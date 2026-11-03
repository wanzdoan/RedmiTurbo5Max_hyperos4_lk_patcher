// ffff0000907680f4  size=148  FUN_ffff0000907680f4

void FUN_ffff0000907680f4(undefined8 param_1)

{
  int iVar1;
  char *pcVar2;
  undefined4 uVar3;
  
  iVar1 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907d72cf,3);
  if (iVar1 == 0) {
    uVar3 = 1;
    pcVar2 = s__FASTBOOT__oem_dfd_on__ffff0000907ff9f2;
  }
  else {
    iVar1 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907ed945,4);
    if (iVar1 != 0) {
      return;
    }
    uVar3 = 0;
    pcVar2 = s__FASTBOOT__oem_dfd_off__ffff0000907d83ba;
  }
  FUN_ffff00009077de54(pcVar2);
  FUN_ffff00009072acfc(7,uVar3);
  FUN_ffff000090709d50(&DAT_ffff0000907df295);
  return;
}


