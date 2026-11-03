
void FUN_ffff00009070b454(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  undefined4 uVar3;
  
  iVar1 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907d7365,3);
  if (iVar1 == 0) {
    uVar3 = 1;
    pcVar2 = s__FASTBOOT__oem_db_encrypt_on__ffff0000907f25ef;
  }
  else {
    iVar1 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907edab6,4);
    if (iVar1 != 0) {
      return;
    }
    uVar3 = 0;
    pcVar2 = s__FASTBOOT__oem_db_encrypt_off__ffff0000907ef3be;
  }
  FUN_ffff00009077e11c(pcVar2);
  FUN_ffff00009076729c(uVar3);
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
}


