// ffff00009070b3dc  size=144  FUN_ffff00009070b3dc

void FUN_ffff00009070b3dc(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  undefined4 uVar3;
  
  iVar1 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907d72cf,3);
  if (iVar1 == 0) {
    uVar3 = 1;
    pcVar2 = s__FASTBOOT__oem_db_encrypt_on__ffff0000907f2463;
  }
  else {
    iVar1 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907ed945,4);
    if (iVar1 != 0) {
      return;
    }
    uVar3 = 0;
    pcVar2 = s__FASTBOOT__oem_db_encrypt_off__ffff0000907ef24d;
  }
  FUN_ffff00009077de54(pcVar2);
  FUN_ffff000090767004(uVar3);
  FUN_ffff000090709d50(&DAT_ffff0000907df295);
  return;
}


