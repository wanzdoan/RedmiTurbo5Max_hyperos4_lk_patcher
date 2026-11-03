
int FUN_ffff0000be055f04(undefined8 param_1,int param_2,undefined8 param_3,int param_4)

{
  char cVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  int local_48;
  int local_44;
  
  iVar2 = FUN_ffff0000be0553b4();
  if ((-1 < iVar2) && (local_48 = 0, iVar2 = param_2, -1 < param_2)) {
    lVar5 = (long)param_4;
    do {
      if (local_48 == 1) {
        local_44 = 0;
        lVar4 = FUN_ffff0000be056248(param_1,iVar2,&local_44);
        if (((lVar4 != 0) && (param_4 <= local_44)) &&
           (iVar3 = FUN_ffff0000be060778(lVar4,param_3,lVar5), iVar3 == 0)) {
          cVar1 = *(char *)(lVar4 + lVar5);
          if (cVar1 == '\0') {
            return iVar2;
          }
          lVar4 = FUN_ffff0000be060748(param_3,0x40,lVar5);
          if ((lVar4 == 0) && (cVar1 == '@')) {
            return iVar2;
          }
        }
      }
      iVar2 = FUN_ffff0000be055848(param_1,iVar2,&local_48);
    } while ((-1 < iVar2) && (-1 < local_48));
    if (local_48 < 0) {
      iVar2 = -1;
    }
  }
  return iVar2;
}


