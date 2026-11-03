// ffff0000907675d8  size=132  FUN_ffff0000907675d8

void FUN_ffff0000907675d8(undefined8 param_1,long param_2,int param_3)

{
  int iVar1;
  undefined8 uVar2;
  char *pcVar3;
  
  if ((param_2 == 0) || (param_3 == 0)) {
    pcVar3 = s_AuthToken_is_empty__failed__ffff0000907ecf14;
  }
  else {
    uVar2 = FUN_ffff00009077ef88(param_2);
    iVar1 = FUN_ffff0000907cda58(param_2,uVar2);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000907ce0e0();
      if (iVar1 == 0) {
        FUN_ffff000090709d50(&DAT_ffff0000907df295);
        return;
      }
      pcVar3 = s_CheckIdMgrAuthToken_failed__ffff0000907eea22;
    }
    else {
      pcVar3 = s_InstallIdMgrAuthToken_failed__ffff0000907fc195;
    }
  }
  FUN_ffff000090709d10(pcVar3);
  return;
}


