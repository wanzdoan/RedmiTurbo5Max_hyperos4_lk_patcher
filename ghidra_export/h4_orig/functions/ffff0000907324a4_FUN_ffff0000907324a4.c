// ffff0000907324a4  size=256  FUN_ffff0000907324a4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000907324a4(undefined8 param_1,long param_2,uint param_3)

{
  int iVar1;
  long lVar2;
  uint uVar3;
  int iVar4;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  ulong local_58;
  undefined8 local_50;
  uint local_48;
  
  if (param_2 == 0) {
    uVar3 = 0xffffffff;
  }
  else if (_DAT_ffff00009086da80 == 0) {
    uVar3 = 0xfffffffe;
  }
  else {
    if (0 < _DAT_ffff00009086da94) {
      iVar4 = 0;
      local_48 = 0;
      local_58 = 0;
      local_50 = 0;
      local_68 = 0;
      uStack_60 = 0;
      local_78 = 0;
      uStack_70 = 0;
      local_88 = 0;
      uStack_80 = 0;
      local_90 = 0;
      lVar2 = _DAT_ffff00009086da80;
      do {
        FUN_ffff00009077eb20(&local_90,lVar2,0x4c);
        local_58 = local_58 & 0xffffffffffffff;
        iVar1 = FUN_ffff00009077f0ac(&local_90,param_1);
        if (iVar1 == 0) {
          uVar3 = local_50._4_4_;
          if (param_3 <= local_50._4_4_) {
            uVar3 = param_3;
          }
          FUN_ffff00009077eb20
                    (param_2,_DAT_ffff00009086da80 + (local_50 & 0xffffffff),(long)(int)uVar3);
          return uVar3;
        }
        iVar4 = iVar4 + 1;
        lVar2 = _DAT_ffff00009086da80 + (ulong)local_48;
      } while (iVar4 < _DAT_ffff00009086da94);
    }
    uVar3 = 0xfffffffd;
  }
  return uVar3;
}


