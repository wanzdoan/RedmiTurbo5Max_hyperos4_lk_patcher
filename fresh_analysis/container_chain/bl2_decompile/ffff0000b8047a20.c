
int FUN_ffff0000b8047a20(undefined8 param_1,int param_2,undefined8 param_3,int param_4)

{
  char cVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  long lVar5;
  int local_48;
  int local_44;
  
  iVar2 = FUN_ffff0000b8047008();
  if ((-1 < iVar2) && (local_48 = 0, iVar2 = param_2, -1 < param_2)) {
    lVar5 = (long)param_4;
    do {
      if (local_48 == 1) {
        local_44 = 0;
        lVar4 = FUN_ffff0000b8047d64(param_1,iVar2,&local_44);
        if (((lVar4 != 0) && (param_4 <= local_44)) &&
           (iVar3 = FUN_ffff0000b804cd08(lVar4,param_3,lVar5), iVar3 == 0)) {
          cVar1 = *(char *)(lVar4 + lVar5);
          if (cVar1 == '\0') {
            return iVar2;
          }
          lVar4 = FUN_ffff0000b804ccd8(param_3,0x40,lVar5);
          if ((lVar4 == 0) && (cVar1 == '@')) {
            return iVar2;
          }
        }
      }
      iVar2 = FUN_ffff0000b804749c(param_1,iVar2,&local_48);
    } while ((-1 < iVar2) && (-1 < local_48));
    if (local_48 < 0) {
      iVar2 = -1;
    }
  }
  return iVar2;
}


