
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000be0255c0(void)

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
  uVar3 = FUN_ffff0000be063c7c();
  if ((uVar3 & 1) == 0) {
    local_28 = FUN_ffff0000be01c3b4
                         (0x120000,0x10000,0xc0000000,0,0,s_MCUPM_reserved_ffff0000be0c2bf5);
  }
  else {
    iVar1 = FUN_ffff0000be065530(s_MCUPM_reserved_ffff0000be0c2bf5,&local_28,&local_50);
    if (iVar1 < 0) {
      _DAT_ffff00000011b180 = 0xdfd987;
      FUN_ffff0000be05fd98(s_MCUPM_err__get_reserved_in_fail_ffff0000be0a6ca2);
      return -5;
    }
  }
  iVar1 = FUN_ffff0000be05d9a8
                    (PTR_DAT_ffff0000be0f5570,s_vm_MCUPM_reserved_ffff0000be0c3f91,0x120000,
                     &local_48,0xc,local_28,0,0);
  lVar4 = local_48;
  if (iVar1 == 0) {
    lVar6 = local_48 + 0x18;
    if ((uVar3 & 1) == 0) {
      iVar1 = FUN_ffff0000be066394
                        (s_mcupm_ffff0000be0bbe22,s_tinysys_mcupm_RV33_A_ffff0000be0c61a8,lVar6,
                         0x120000);
      if (iVar1 < 0) {
        FUN_ffff0000be05fd98(s_mcupm_load_from_partition_failed_ffff0000be0a59c8,iVar1);
        FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,local_48);
        goto LAB_ffff0000be025678;
      }
    }
    else {
      iVar1 = 0;
    }
    FUN_ffff0000be060ae0(local_48,0,0x18);
    if (*(int *)(lVar4 + 0x18) == 0x58901690) {
      lVar6 = lVar6 + (ulong)*(uint *)(lVar4 + 0x1c);
      FUN_ffff0000be05fd98(s__s_LEGACY_XFILE_format_ffff0000be0a4712,s__MCUPMLDR__ffff0000be0be278);
    }
    _DAT_ffff00000c000cd8 = _DAT_ffff00000c000cd8 & 0xfffffffe;
    iVar2 = FUN_ffff0000be025340();
    if (iVar2 == 0) {
      uVar5 = 0x2304;
      _DAT_ffff00000c240000 = 0;
      lVar4 = 0;
      do {
        uVar5 = uVar5 - 4;
        *(undefined4 *)(lVar4 + -0xfffff3db0000) = *(undefined4 *)(lVar6 + lVar4);
        lVar4 = lVar4 + 4;
      } while (4 < uVar5);
      _DAT_ffff00000c250004 = FUN_ffff0000be05d228(lVar6);
      _DAT_ffff00000c250008 = 0x2300;
      _DAT_ffff00000c25000c = FUN_ffff0000be05d228(lVar6 + 0x2300);
      _DAT_ffff00000c250010 = 0x7d27c;
      _DAT_ffff00000c250014 = FUN_ffff0000be05d228(lVar6 + 0x80000);
      _DAT_ffff00000c250018 = 0x80000;
      FUN_ffff0000be007328(local_48,0x120000);
      _DAT_ffff00000c2cf590 = (undefined4)local_28;
      _DAT_ffff00000c2cf594 = FUN_ffff0000be063c7c();
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
    FUN_ffff0000be05fd98(s_MCUPM_vmm_alloc_physical_failed__ffff0000be0b8965,iVar1);
LAB_ffff0000be025678:
    FUN_ffff0000be01c51c(local_28);
  }
  return iVar1;
}


