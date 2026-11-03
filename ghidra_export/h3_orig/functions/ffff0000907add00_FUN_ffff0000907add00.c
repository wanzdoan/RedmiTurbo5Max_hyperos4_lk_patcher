// ffff0000907add00  size=352  FUN_ffff0000907add00

undefined8
FUN_ffff0000907add00(long param_1,uint param_2,uint param_3,undefined8 param_4,uint param_5)

{
  bool bVar1;
  int iVar2;
  undefined8 uVar3;
  undefined8 local_130;
  undefined8 uStack_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  
  local_50 = 0;
  uStack_48 = 0;
  local_60 = 0;
  uStack_58 = 0;
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  uStack_e8 = 0;
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  local_130 = 0;
  uStack_128 = 0;
  if ((param_5 & 0xffffffef) == 0x20) {
    bVar1 = param_5 == 0x30;
    if (param_3 < 0x10) {
      FUN_ffff00009077eb88(param_4,0,0x40);
      if (param_3 == 0) {
        uVar3 = FUN_ffff0000907a262c(param_1,param_2,param_4,bVar1);
        iVar2 = (int)uVar3;
      }
      else {
        FUN_ffff00009077eb88(&local_130,0,0xe0);
        uVar3 = FUN_ffff0000907a2590(&local_130,bVar1);
        if ((int)uVar3 != 0) {
          return uVar3;
        }
        uVar3 = FUN_ffff0000907a25c4(&local_130,param_1,(ulong)(param_2 & 0xfffffff0),bVar1);
        if ((int)uVar3 != 0) {
          return uVar3;
        }
        FUN_ffff0000907acf74(&local_50,param_1 + (ulong)(param_2 & 0xfffffff0),param_2 & 0xf);
        uVar3 = FUN_ffff0000907a25c4(&local_130,&local_50,0x10,bVar1);
        if ((int)uVar3 != 0) {
          return uVar3;
        }
        uVar3 = FUN_ffff0000907a25f8(&local_130,param_4,bVar1);
        iVar2 = (int)uVar3;
      }
      if (iVar2 == 0) {
        uVar3 = 0;
      }
    }
    else {
      uVar3 = 0x10101a;
    }
  }
  else {
    uVar3 = 0xffffffff;
  }
  return uVar3;
}


