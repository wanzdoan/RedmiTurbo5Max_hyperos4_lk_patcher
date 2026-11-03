// ffff000090740720  size=256  FUN_ffff000090740720

/* WARNING: Type propagation algorithm not settling */

void FUN_ffff000090740720(code *param_1)

{
  int iVar1;
  undefined8 uVar2;
  ulong uVar3;
  char *pcVar4;
  long local_38 [3];
  
  local_38[2] = 0;
  local_38[0] = 0;
  local_38[1] = 0;
  iVar1 = FUN_ffff000090783718(s_SSPM_reserved_ffff0000907cf998,local_38 + 2,local_38 + 1);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c(s_sspm_err__get_reserved_in_fail_ffff0000907e96cb);
    return;
  }
  if (local_38[2] == 0) {
    FUN_ffff00009077e11c(s_sspm_mblock_allocation_failed_ffff0000907e1796);
    return;
  }
  iVar1 = FUN_ffff00009077ba64
                    (DAT_ffff000090839928,s_vm_SSPM_reserved_ffff0000907d7c0a,0x280000,local_38,0xc,
                     local_38[2],0,0);
  if (iVar1 < 0) {
    FUN_ffff00009077e11c(s_sspm_vmm_alloc_physical_failed__e_ffff0000907ec7f7,iVar1);
  }
  else {
    iVar1 = *(int *)(local_38[0] + 0x14);
    if (iVar1 == 0) {
      pcVar4 = s__SSPM__AEE_EXPDB_without_xfile_ffff000090800d66;
    }
    else {
      uVar2 = FUN_ffff00009077b334(local_38[0] + (ulong)*(uint *)(local_38[0] + 0x10));
      uVar3 = (*param_1)(uVar2,iVar1);
      if ((uVar3 & 1) != 0) goto LAB_ffff0000907407fc;
      pcVar4 = s__SSPM__AEE_EXPDB_xifle_fail_ffff0000907efcfb;
    }
    FUN_ffff00009077e11c(pcVar4);
  }
LAB_ffff0000907407fc:
  FUN_ffff00009077be30(DAT_ffff000090839928,local_38[0]);
  return;
}


