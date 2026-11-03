// ffff0000907c69bc  size=540  FUN_ffff0000907c69bc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907c69bc(long param_1,uint param_2)

{
  uint uVar1;
  undefined4 *puVar2;
  int iVar3;
  undefined4 uVar4;
  undefined8 uVar5;
  long lVar6;
  uint uVar7;
  ulong uVar8;
  undefined8 local_60;
  int local_58;
  
  iVar3 = _DAT_ffff00009090e618;
  if (0x4b < param_2) {
    local_58 = 0;
    local_60 = 0;
    thunk_FUN_ffff00009077eb20(&local_60,param_1 + 0x40,0xc);
    uVar4 = FUN_ffff0000907c1050(local_60 & 0xffffffff);
    local_60 = CONCAT44(local_60._4_4_,uVar4);
    uVar4 = FUN_ffff0000907c1050(local_60._4_4_);
    local_60 = CONCAT44(uVar4,(int)local_60);
    local_58 = FUN_ffff0000907c1050(local_58);
    iVar3 = 0xc;
    if ((int)local_60 == -0x6f86f7fc) {
      uVar8 = 0;
      uVar7 = 0x4c;
      do {
        lVar6 = uVar8 * 0x20;
        puVar2 = (undefined4 *)(_DAT_ffff00009090e600 + lVar6);
        puVar2[1] = local_60._4_4_;
        puVar2[2] = local_58;
        *puVar2 = 0x90790804;
        uVar5 = FUN_ffff0000907c1544(local_60._4_4_);
        *(undefined8 *)(_DAT_ffff00009090e600 + lVar6 + 0x10) = uVar5;
        lVar6 = *(long *)(_DAT_ffff00009090e600 + lVar6 + 0x10);
        if (lVar6 == 0) {
          return 0x100006;
        }
        if ((param_2 < local_60._4_4_ + uVar7) || (local_60._4_4_ + uVar7 < uVar7)) {
          return 0x100001;
        }
        thunk_FUN_ffff00009077eb20(lVar6,param_1 + (ulong)uVar7);
        iVar3 = local_60._4_4_;
        uVar5 = FUN_ffff0000907c1544(local_58);
        *(undefined8 *)(_DAT_ffff00009090e600 + uVar8 * 0x20 + 0x18) = uVar5;
        lVar6 = *(long *)(_DAT_ffff00009090e600 + uVar8 * 0x20 + 0x18);
        if (lVar6 == 0) {
          return 0x100006;
        }
        uVar7 = iVar3 + uVar7;
        if (param_2 < local_58 + uVar7) {
          return 0x100001;
        }
        if (local_58 + uVar7 < uVar7) {
          return 0x100001;
        }
        thunk_FUN_ffff00009077eb20(lVar6,param_1 + (ulong)uVar7);
        uVar1 = local_58 + uVar7;
        if (0xfffffff3 < uVar1) {
          return 0;
        }
        uVar7 = uVar1 + 0xc;
        if (param_2 < uVar7) {
          return 0;
        }
        if (param_1 != 0) {
          thunk_FUN_ffff00009077eb20(&local_60,param_1 + (ulong)uVar1,0xc);
          uVar4 = FUN_ffff0000907c1050(local_60 & 0xffffffff);
          local_60 = CONCAT44(local_60._4_4_,uVar4);
          uVar4 = FUN_ffff0000907c1050(local_60._4_4_);
          local_60 = CONCAT44(uVar4,(int)local_60);
          local_58 = FUN_ffff0000907c1050(local_58);
        }
        uVar8 = (ulong)((int)uVar8 + 1);
      } while ((int)local_60 == -0x6f86f7fc);
      iVar3 = uVar1 - 0x34;
    }
  }
  _DAT_ffff00009090e618 = iVar3;
  return 0;
}


