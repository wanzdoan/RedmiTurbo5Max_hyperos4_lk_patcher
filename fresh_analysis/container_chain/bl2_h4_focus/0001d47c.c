// File offset entry: 0x1d47c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000b801d47c(void)

{
  int iVar1;
  int iVar2;
  ulong uVar3;
  long lVar4;
  uint uVar5;
  long lVar6;
  undefined8 local_50;
  long local_48;
  undefined8 local_28;
  
  local_28 = 0;
  local_50 = 0;
  local_48 = 0;
  uVar3 = FUN_ffff0000b804f554();
  if ((uVar3 & 1) == 0) {
    local_28 = FUN_ffff0000b800c9f4
                         (0x120000,0x10000,0xc0000000,0,0,s_MCUPM_reserved_ffff0000b809e4d1);
  }
  else {
    iVar1 = FUN_ffff0000b80501b0(s_MCUPM_reserved_ffff0000b809e4d1,&local_28,&local_50);
    if (iVar1 < 0) {
      _DAT_ffff00000011b180 = 0xdfd987;
      FUN_ffff0000b804c33c(s_MCUPM_err__get_reserved_in_fail_ffff0000b808805b);
      return -5;
    }
  }
  iVar1 = FUN_ffff0000b804a5f8
                    (PTR_DAT_ffff0000b81086d8,s_vm_MCUPM_reserved_ffff0000b809f676,0x120000,
                     &local_48,0xc,local_28,0,0);
  lVar4 = local_48;
  if (iVar1 == 0) {
    lVar6 = local_48 + 0x18;
    if ((uVar3 & 1) == 0) {
      iVar1 = FUN_ffff0000b8050eb8
                        (s_mcupm_ffff0000b8098c15,s_tinysys_mcupm_RV33_A_ffff0000b80a0fa7,lVar6,
                         0x120000);
      if (iVar1 < 0) {
        FUN_ffff0000b804c33c(s_mcupm_load_from_partition_failed_ffff0000b808713c,iVar1);
        FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_48);
        goto LAB_ffff0000b801d534;
      }
    }
    else {
      iVar1 = 0;
    }
    FUN_ffff0000b804d070(local_48,0,0x18);
    if (*(int *)(lVar4 + 0x18) == 0x58901690) {
      lVar6 = lVar6 + (ulong)*(uint *)(lVar4 + 0x1c);
      FUN_ffff0000b804c33c(s__s_LEGACY_XFILE_format_ffff0000b8086301,s__MCUPMLDR__ffff0000b809a918);
    }
    _DAT_ffff00000c000cd8 = _DAT_ffff00000c000cd8 & 0xfffffffe;
    iVar2 = FUN_ffff0000b801d1fc();
    if (iVar2 == 0) {
      uVar5 = 0x2304;
      _DAT_ffff00000c240000 = 0;
      lVar4 = 0;
      do {
        uVar5 = uVar5 - 4;
        *(undefined4 *)(lVar4 + -0xfffff3db0000) = *(undefined4 *)(lVar6 + lVar4);
        lVar4 = lVar4 + 4;
      } while (4 < uVar5);
      _DAT_ffff00000c250004 = FUN_ffff0000b8049ec8(lVar6);
      _DAT_ffff00000c250008 = 0x2300;
      _DAT_ffff00000c25000c = FUN_ffff0000b8049ec8(lVar6 + 0x2300);
      _DAT_ffff00000c250010 = 0x7d27c;
      _DAT_ffff00000c250014 = FUN_ffff0000b8049ec8(lVar6 + 0x80000);
      _DAT_ffff00000c250018 = 0x80000;
      FUN_ffff0000b80075ac(local_48,0x120000);
      _DAT_ffff00000c2cf590 = (undefined4)local_28;
      _DAT_ffff00000c2cf594 = FUN_ffff0000b804f554();
      _DAT_ffff00000c2cf594 = _DAT_ffff00000c2cf594 & 1;
      _DAT_ffff00000c2cf5c8 = _DAT_ffff00000c250014;
      _DAT_ffff00000c2cf5cc = _DAT_ffff00000c250018;
      _DAT_ffff00000c2cf5ec = 0;
      _DAT_ffff00000c2cf5fc = 0;
      if ((_DAT_ffff00000c2401f8 & 1) == 0) {
        iVar1 = 0;
        _DAT_ffff00000c240000 = 0x5c00001f;
      }
      else {
        iVar1 = 0;
        _DAT_ffff00000c240000 = 0x5c00005f;
      }
    }
  }
  else {
    FUN_ffff0000b804c33c(s_MCUPM_vmm_alloc_physical_failed__ffff0000b80963a7,iVar1);
LAB_ffff0000b801d534:
    FUN_ffff0000b800d264(local_28);
  }
  return iVar1;
}


