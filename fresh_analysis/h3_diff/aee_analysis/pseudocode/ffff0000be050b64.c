
void FUN_ffff0000be050b64(undefined8 param_1,long param_2,int param_3)

{
  int iVar1;
  undefined8 uVar2;
  char *pcVar3;
  
  if ((param_2 == 0) || (param_3 == 0)) {
    pcVar3 = s_AuthToken_is_empty__failed__ffff0000be0b7e8f;
  }
  else {
    uVar2 = FUN_ffff0000be060e5c(param_2);
    iVar1 = FUN_ffff0000be09b06c(param_2,uVar2);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000be09b6f4();
      if (iVar1 == 0) {
        FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
        return;
      }
      pcVar3 = s_CheckIdMgrAuthToken_failed__ffff0000be0b914c;
    }
    else {
      pcVar3 = s_InstallIdMgrAuthToken_failed__ffff0000be0c2258;
    }
  }
  FUN_ffff0000be0137e8(pcVar3);
  return;
}


