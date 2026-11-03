// ffff000090767c10  size=184  FUN_ffff000090767c10

void FUN_ffff000090767c10(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined *puVar3;
  
  uVar2 = FUN_ffff00009077f250(&DAT_ffff0000907f3fed);
  iVar1 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907f3fed,uVar2);
  if (iVar1 == 0) {
    FUN_ffff0000907205cc(1);
    puVar3 = &DAT_ffff0000907d8c2f;
  }
  else {
    uVar2 = FUN_ffff00009077f250(&DAT_ffff0000907f938c);
    iVar1 = FUN_ffff00009077f348(param_1,&DAT_ffff0000907f938c,uVar2);
    if (iVar1 != 0) {
      FUN_ffff000090709bec(s_unknown_argument_ffff0000907f5391);
      goto LAB_ffff000090767cb0;
    }
    FUN_ffff0000907205cc();
    puVar3 = &DAT_ffff0000907d2506;
  }
  FUN_ffff0000907099c4(s_off_mode_charge_ffff0000907fcb2a,puVar3);
LAB_ffff000090767cb0:
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
}


