
void FUN_ffff0000be050f04(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined *puVar3;
  
  uVar2 = FUN_ffff0000be060e5c(&DAT_ffff0000be0bceec);
  iVar1 = FUN_ffff0000be060f54(param_1,&DAT_ffff0000be0bceec,uVar2);
  if (iVar1 == 0) {
    FUN_ffff0000be01e8ec(1);
    puVar3 = &DAT_ffff0000be0aa1ff;
  }
  else {
    uVar2 = FUN_ffff0000be060e5c(&DAT_ffff0000be0c0595);
    iVar1 = FUN_ffff0000be060f54(param_1,&DAT_ffff0000be0c0595,uVar2);
    if (iVar1 != 0) {
      FUN_ffff0000be013738(s_unknown_argument_ffff0000be0bd896);
      goto LAB_ffff0000be050fa4;
    }
    FUN_ffff0000be01e8ec();
    puVar3 = &DAT_ffff0000be0a553d;
  }
  FUN_ffff0000be013510(s_off_mode_charge_ffff0000be0c2a02,puVar3);
LAB_ffff0000be050fa4:
  FUN_ffff0000be013828(&DAT_ffff0000be0ae340);
  return;
}


