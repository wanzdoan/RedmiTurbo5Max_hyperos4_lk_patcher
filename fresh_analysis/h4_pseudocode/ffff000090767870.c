
void FUN_ffff000090767870(undefined8 param_1,long param_2,int param_3)

{
  int iVar1;
  undefined8 uVar2;
  char *pcVar3;
  
  if ((param_2 == 0) || (param_3 == 0)) {
    pcVar3 = s_AuthToken_is_empty__failed__ffff0000907ed097;
  }
  else {
    uVar2 = FUN_ffff00009077f250(param_2);
    iVar1 = FUN_ffff0000907ce0cc(param_2,uVar2);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000907ce754();
      if (iVar1 == 0) {
        FUN_ffff000090709cdc(&DAT_ffff0000907df338);
        return;
      }
      pcVar3 = s_CheckIdMgrAuthToken_failed__ffff0000907eeb93;
    }
    else {
      pcVar3 = s_InstallIdMgrAuthToken_failed__ffff0000907fc331;
    }
  }
  FUN_ffff000090709c9c(pcVar3);
  return;
}


