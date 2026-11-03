// ffff0000907c62f4  size=732  FUN_ffff0000907c62f4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907c62f4(long param_1,undefined8 param_2)

{
  char *pcVar1;
  undefined4 uVar2;
  int iVar3;
  long lVar4;
  uint uVar5;
  long lVar6;
  long lVar7;
  uint uVar8;
  uint uVar9;
  ulong uVar10;
  undefined8 local_78;
  undefined4 local_70;
  long local_68;
  
  local_68 = 0;
  if (_DAT_ffff00009090e60c == 0) {
    return 0x10800d;
  }
  if (_DAT_ffff00009090e608 != 0) {
    local_70 = 0;
    local_78 = 0;
    FUN_ffff0000907c5198(param_1,param_2,&local_68);
    if (local_68 == 0) {
      return 0x10800b;
    }
    if ((uint)local_68 < (int)_DAT_ffff00009090e610 + 0x40U) {
      return 0x100001;
    }
    uVar8 = 0;
    lVar7 = _DAT_ffff00009090e610 + 0x40;
    do {
      uVar5 = uVar8;
      if (uVar8 < 8) {
        uVar5 = 7;
      }
      uVar10 = (ulong)uVar8 << 5 | 0x10;
      uVar9 = uVar8;
      while (*(int *)(_DAT_ffff00009090e600 + uVar10 + -0x10) != -0x6f86f7fc) {
        uVar10 = uVar10 + 0x20;
        uVar9 = uVar9 + 1;
        if (uVar5 + 1 == uVar9) goto LAB_ffff0000907c64e0;
      }
      thunk_FUN_ffff00009077eb20(&local_78,_DAT_ffff00009090e600 + uVar10 + -0x10,0xc);
      uVar2 = FUN_ffff0000907c1070(local_78 & 0xffffffff);
      local_78 = CONCAT44(local_78._4_4_,uVar2);
      uVar2 = FUN_ffff0000907c1070(local_78._4_4_);
      local_78 = CONCAT44(uVar2,(undefined4)local_78);
      local_70 = FUN_ffff0000907c1070(local_70);
      iVar3 = (**(code **)(param_1 + 0x28))(param_1,param_2,lVar7,0xc,&local_78);
      if (iVar3 != 0) {
LAB_ffff0000907c65b4:
        FUN_ffff00009077e11c(s_Error_writing_to_partition__s__ffff0000907e8ae4,param_2);
        return iVar3;
      }
      lVar7 = lVar7 + 0xc;
      iVar3 = *(int *)(_DAT_ffff00009090e600 + uVar10 + -0xc);
      uVar8 = iVar3 + (uint)lVar7;
      if (uVar8 < (uint)lVar7) {
        return 0x100001;
      }
      if ((uint)local_68 < uVar8) {
        return 0x100001;
      }
      iVar3 = (**(code **)(param_1 + 0x28))
                        (param_1,param_2,lVar7,iVar3,*(undefined8 *)(_DAT_ffff00009090e600 + uVar10)
                        );
      if (iVar3 != 0) goto LAB_ffff0000907c65b4;
      iVar3 = *(int *)(_DAT_ffff00009090e600 + uVar10 + -8);
      lVar7 = lVar7 + (ulong)*(uint *)(_DAT_ffff00009090e600 + uVar10 + -0xc);
      uVar8 = iVar3 + (uint)lVar7;
      if ((uint)local_68 < uVar8) {
        return 0x100001;
      }
      if (uVar8 < (uint)lVar7) {
        return 0x100001;
      }
      iVar3 = (**(code **)(param_1 + 0x28))
                        (param_1,param_2,lVar7,iVar3,
                         *(undefined8 *)(_DAT_ffff00009090e600 + uVar10 + 8));
      if (iVar3 != 0) goto LAB_ffff0000907c65b4;
      uVar8 = uVar9 + 1;
      lVar7 = lVar7 + (ulong)*(uint *)(_DAT_ffff00009090e600 + uVar10 + -8);
    } while (uVar9 < 7);
LAB_ffff0000907c64e0:
    uVar10 = (lVar7 - _DAT_ffff00009090e610) - 0x40;
    if (uVar10 < _DAT_ffff00009090e618) {
      lVar6 = _DAT_ffff00009090e618 - uVar10;
      lVar4 = FUN_ffff0000907c1544(lVar6);
      if (lVar4 != 0) {
        thunk_FUN_ffff00009077ee50(lVar4,0,lVar6);
        iVar3 = (**(code **)(param_1 + 0x28))(param_1,param_2,lVar7,lVar6,lVar4);
        pcVar1 = s_Write_persist_value_to__s__ffff0000907ed968;
        if (iVar3 != 0) {
          pcVar1 = s_Error_writing_to_partition__s__ffff0000907e8ae4;
        }
        FUN_ffff00009077e11c(pcVar1,param_2);
        FUN_ffff0000907c1014(lVar4);
        return iVar3;
      }
      return 1;
    }
    FUN_ffff00009077e11c(s_Write_persist_value_to__s__ffff0000907ed968,param_2);
  }
  return 0;
}


