// ffff00009072139c  size=864  FUN_ffff00009072139c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009072139c(code *param_1)

{
  uint uVar1;
  uint uVar2;
  int iVar3;
  char *pcVar4;
  long lVar5;
  undefined8 uVar6;
  ulong uVar7;
  int *piVar8;
  uint uVar9;
  long lVar10;
  undefined8 local_70;
  uint local_68;
  undefined8 local_48;
  
  iVar3 = FUN_ffff00009072b748();
  if (iVar3 == 0) {
    iVar3 = FUN_ffff00009072b620();
    if (iVar3 == 0) {
      lVar5 = FUN_ffff00009077d24c(0x80000);
      if (lVar5 != 0) {
        FUN_ffff00009077de54(s__CPU_SYS_PI__malloc_success_size_ffff0000907e5dc1,0x80000,0x80000);
        FUN_ffff00009077eb88(lVar5,0,0x80000);
        local_68 = 0;
        local_70 = 0;
        local_48 = 0x1a5a5beef;
        FUN_ffff00009077eb88(&local_70,0,0xc);
        local_70._0_4_ = 1;
        local_68 = 0x800;
        FUN_ffff00009077e858(lVar5 + 8,&local_70,0xc);
        local_68 = 0x800;
        local_70._0_4_ = 3;
        FUN_ffff00009077e858(lVar5 + 0x14,&local_70,0xc);
        local_70._0_4_ = 5;
        local_68 = 0x4000;
        FUN_ffff00009077e858(lVar5 + 0x20,&local_70,0xc);
        if (_DAT_ffff000000123fd0 >> 0x13 == 0) {
          local_70._0_4_ = 6;
          local_68 = _DAT_ffff000000123fd0;
          FUN_ffff00009077e858(lVar5 + 0x2c,&local_70,0xc);
          uVar9 = 5;
          lVar10 = 0x38;
        }
        else {
          uVar9 = 4;
          lVar10 = 0x2c;
        }
        local_70 = CONCAT44(local_70._4_4_,8);
        local_68 = 0xaf0;
        FUN_ffff00009077e858(lVar5 + lVar10,&local_70,0xc);
        uVar1 = (int)lVar10 + 0xc;
        local_48._4_4_ = uVar9;
        FUN_ffff00009077e858(lVar5,&local_48,8);
        uVar7 = (ulong)local_48._4_4_;
        if (local_48._4_4_ != 0) {
          piVar8 = (int *)(lVar5 + 0x10);
          uVar9 = uVar1;
          do {
            iVar3 = *piVar8;
            piVar8[-1] = uVar9;
            uVar7 = uVar7 - 1;
            piVar8 = piVar8 + 3;
            uVar9 = iVar3 + uVar9;
          } while (uVar7 != 0);
        }
        FUN_ffff00009077e858(lVar5 + (ulong)uVar1,0xffff000000118000,0x800);
        _DAT_ffff00000c240000 = _DAT_ffff00000c240000 & 0xfffffff7;
        FUN_ffff00009077e858(lVar5 + (ulong)(uVar1 | 0x800),0xffff00000c2bd920,0x800);
        uVar2 = uVar1 | 0x5000;
        _DAT_ffff00000c240000 = _DAT_ffff00000c240000 & 0xfffffff7;
        FUN_ffff00009077e858(lVar5 + (ulong)(uVar1 | 0x1000),0xffff000000120000,0x4000);
        uVar9 = _DAT_ffff000000123fd0 + uVar2;
        _DAT_ffff00000c240000 = _DAT_ffff00000c240000 & 0xfffffff7;
        if (uVar9 < 0x80001) {
          FUN_ffff00009077e858(lVar5 + (ulong)uVar2,(ulong)_DAT_ffff000000123fcc - 0xfffff3db0000);
          _DAT_ffff00000c240000 = _DAT_ffff00000c240000 & 0xfffffff7;
          if (uVar9 < 0x7f511) {
            uVar7 = (ulong)uVar9;
            uVar9 = uVar9 + 0xaf0;
            FUN_ffff00009077e858(lVar5 + uVar7,0xffff00000c2bce30,0xaf0);
          }
          else {
            FUN_ffff00009077de54(s__CPU_SYS_PI__MCUPM_HBVC_LOG_SIZE_ffff0000907e43ff,0xaf0);
          }
        }
        else {
          FUN_ffff00009077de54
                    (s__CPU_SYS_PI__MCUPM_TCM_LOG_SIZE_s_ffff0000907edca8,_DAT_ffff000000123fd0);
          uVar9 = uVar2;
        }
        _DAT_ffff00000c240000 = _DAT_ffff00000c240000 | 8;
        uVar6 = FUN_ffff00009077b06c(lVar5);
        (*param_1)(uVar6,uVar9);
        thunk_FUN_ffff0000907c2ad8(lVar5);
        return;
      }
      pcVar4 = s__CPU_SYS_PI__malloc_failed_ffff0000907f9585;
    }
    else {
      pcVar4 = s__CPU_SYS_PI__eb_sleep_protect_fa_ffff0000907f26ed;
    }
  }
  else {
    pcVar4 = s__CPU_SYS_PI__eb_spmc_failed_ffff0000907e75bb;
  }
  FUN_ffff00009077de54(pcVar4);
  return;
}


