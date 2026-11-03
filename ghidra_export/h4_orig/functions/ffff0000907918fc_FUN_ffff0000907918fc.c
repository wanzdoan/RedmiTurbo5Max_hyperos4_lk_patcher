// ffff0000907918fc  size=344  FUN_ffff0000907918fc

undefined8 FUN_ffff0000907918fc(long *param_1)

{
  bool bVar1;
  uint uVar2;
  long lVar3;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  if ((*(uint *)(*param_1 + 0x34) & 1) != 0) {
    FUN_ffff00009078fd9c(param_1);
  }
  FUN_ffff000090792b5c(param_1);
  if (param_1[1] != 0) {
    local_30 = 0;
    uStack_28 = 0;
    local_40 = 0;
    uStack_38 = 0;
    uVar2 = FUN_ffff00009071c6e8();
    if ((uVar2 < 5) && (uVar2 != 2)) {
      *(uint *)(param_1[1] + 0x90) = *(uint *)(param_1[1] + 0x90) | 1;
    }
    else {
      FUN_ffff00009077ee50(&local_40,0,0x20);
      FUN_ffff00009071ebf4(0xc200040d,2,1,0,0,0,0,0,&local_40);
    }
    FUN_ffff00009077cea0(10000);
  }
  *(undefined4 *)(*param_1 + 0x34) = 1;
  FUN_ffff00009077cea0(1000);
  lVar3 = *param_1;
  if ((*(uint *)(lVar3 + 0x34) & 1) == 0) {
    uVar2 = 0xffffffeb;
    do {
      bVar1 = 0xfffffffe < uVar2;
      uVar2 = uVar2 + 1;
      if (bVar1) {
        FUN_ffff00009077e11c(s_Controller_enable_failed_ffff0000907f0809);
        FUN_ffff0000907971a8(param_1);
        FUN_ffff000090797118(param_1,0,0xa0);
        FUN_ffff000090797118(param_1,0x100,0x74);
        return 0xffffffff;
      }
      FUN_ffff00009077cea0(5000);
      lVar3 = *param_1;
    } while ((*(uint *)(lVar3 + 0x34) & 1) == 0);
  }
  *(uint *)(lVar3 + 0x24) = *(uint *)(lVar3 + 0x24) | 0x470;
  return 0;
}


