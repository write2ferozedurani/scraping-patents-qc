export interface Patent {
    patentApplicationNumber: string;
    inventionSubjectMatterCategory: string;
    filingDate: Date;
    assigneeEntityName: string;
    assigneePostalAddressText:string;
    inventionTitle:string;
    abstractText: string;
    filelocationURI: string;
    grantDocumentIdentifier: string;
    grantDate: Date;
    patentNumber: string;
}
