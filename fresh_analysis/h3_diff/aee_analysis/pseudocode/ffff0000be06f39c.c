
undefined8 FUN_ffff0000be06f39c(long *param_1)

{
  bool bVar1;
  uint uVar2;
  long lVar3;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  if ((*(uint *)(*param_1 + 0x34) & 1) != 0) {
    FUN_ffff0000be06d8bc(param_1);
  }
  FUN_ffff0000be0705fc(param_1);
  if (param_1[1] != 0) {
    local_30 = 0;
    uStack_28 = 0;
    local_40 = 0;
    uStack_38 = 0;
    uVar2 = FUN_ffff0000be01d35c();
    if ((uVar2 < 5) && (uVar2 != 2)) {
      *(uint *)(param_1[1] + 0x90) = *(uint *)(param_1[1] + 0x90) | 1;
    }
    else {
      FUN_ffff0000be060ae0(&local_40,0,0x20);
      FUN_ffff0000be01d644(0xc200040d,2,1,0,0,0,0,0,&local_40);
    }
    FUN_ffff0000be05ede4(10000);
  }
  *(undefined4 *)(*param_1 + 0x34) = 1;
  FUN_ffff0000be05ede4(1000);
  lVar3 = *param_1;
  if ((*(uint *)(lVar3 + 0x34) & 1) == 0) {
    uVar2 = 0xffffffeb;
    do {
      bVar1 = 0xfffffffe < uVar2;
      uVar2 = uVar2 + 1;
      if (bVar1) {
        FUN_ffff0000be05fd98(s_Controller_enable_failed_ffff0000be0ba66a);
        FUN_ffff0000be07491c(param_1);
        FUN_ffff0000be07488c(param_1,0,0xa0);
        FUN_ffff0000be07488c(param_1,0x100,0x74);
        return 0xffffffff;
      }
      FUN_ffff0000be05ede4(5000);
      lVar3 = *param_1;
    } while ((*(uint *)(lVar3 + 0x34) & 1) == 0);
  }
  *(uint *)(lVar3 + 0x24) = *(uint *)(lVar3 + 0x24) | 0x470;
  return 0;
}


