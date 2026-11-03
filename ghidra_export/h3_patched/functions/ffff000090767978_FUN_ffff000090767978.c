// ffff000090767978  size=184  FUN_ffff000090767978

void FUN_ffff000090767978(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined *puVar3;
  
  uVar2 = FUN_ffff00009077ef88(&DAT_ffff0000907f3e4a);
  iVar1 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907f3e4a,uVar2);
  if (iVar1 == 0) {
    FUN_ffff000090720444(1);
    puVar3 = &DAT_ffff0000907d8bae;
  }
  else {
    uVar2 = FUN_ffff00009077ef88(&DAT_ffff0000907f91f0);
    iVar1 = FUN_ffff00009077f080(param_1,&DAT_ffff0000907f91f0,uVar2);
    if (iVar1 != 0) {
      FUN_ffff000090709c60(s_unknown_argument_ffff0000907f520d);
      goto LAB_ffff000090767a18;
    }
    FUN_ffff000090720444();
    puVar3 = &DAT_ffff0000907d24c9;
  }
  FUN_ffff000090709a38(s_off_mode_charge_ffff0000907fc98e,puVar3);
LAB_ffff000090767a18:
  FUN_ffff000090709d50(&DAT_ffff0000907df27e);
  return;
}


