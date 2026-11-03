// ffff00009076793c  size=148  FUN_ffff00009076793c

void FUN_ffff00009076793c(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  undefined4 uVar3;
  
  iVar1 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907d7365,3);
  if (iVar1 == 0) {
    uVar3 = 1;
    pcVar2 = s__FASTBOOT__oem_p2u_on__ffff0000907ffb54;
  }
  else {
    iVar1 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907edab6,4);
    if (iVar1 != 0) {
      return;
    }
    uVar3 = 0;
    pcVar2 = s__FASTBOOT__oem_p2u_off__ffff0000907eb743;
  }
  FUN_ffff00009077e11c(pcVar2);
  FUN_ffff00009072af40(0,uVar3);
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
}


