// ffff000090740a70  size=356  FUN_ffff000090740a70

void FUN_ffff000090740a70(undefined8 param_1)

{
  uint uVar1;
  undefined4 uVar2;
  int iVar3;
  undefined4 uVar4;
  uint *puVar5;
  long lVar6;
  undefined8 uVar7;
  ulong uVar8;
  undefined4 local_54;
  long local_50;
  long lStack_48;
  undefined8 local_40;
  undefined8 uStack_38;
  
  local_54 = 0;
  uVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_sspm_ffff0000907cf987);
  puVar5 = (uint *)FUN_ffff00009076dcd4
                             (param_1,uVar2,s_sspm_res_ram_size_ffff0000907efba7,&local_54);
  if (puVar5 != (uint *)0x0) {
    uVar1 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
    uVar8 = (ulong)(uVar1 >> 0x10 | uVar1 << 0x10);
    lVar6 = FUN_ffff000090719798(uVar8,0x10000,0x9fffffff,0,0,s_sspm_ap_shared_ffff0000907d7b85);
    FUN_ffff00009077de54(s_sspm__malloc_for_ap_shared_start_ffff0000907d2e7e,lVar6,uVar8);
    iVar3 = FUN_ffff00009071c57c();
    if (iVar3 != 0) {
      uVar7 = FUN_ffff000090719798
                        (uVar8,0x10000,0xffffffffffffffff,lVar6 + 0x400000000,0,
                         s_sspm_ap_shared_ffff0000907d7b85);
      FUN_ffff00009077de54(s_sspm__mirror_malloc_for_ap_share_ffff0000907fd564,uVar7,uVar8);
    }
    if (lVar6 == 0) {
      FUN_ffff00009077de54(s_sspm_ap_shared_allocation_failed_ffff0000907ff1de);
    }
    lStack_48 = lVar6 + uVar8;
    local_40 = 0xb6db6800000012;
    uStack_38 = 0xb6db69;
    local_50 = lVar6;
    uVar4 = FUN_ffff00009078ae3c(&local_50);
    FUN_ffff00009077de54(s_sspm_enable_share_emimpu_ret__d_ffff0000907e6356,uVar4);
    uVar1 = ((uint)lVar6 & 0xff00ff00) >> 8 | ((uint)lVar6 & 0xff00ff) << 8;
    local_50 = CONCAT44(local_50._4_4_,uVar1 >> 0x10 | uVar1 << 0x10);
    iVar3 = FUN_ffff00009076e40c(param_1,uVar2,s_sspm_res_ram_start_ffff0000907d7b94,&local_50,4);
    if (iVar3 < 0) {
      FUN_ffff00009077de54(s_sspm_set_sspm_res_ram_start_fail_ffff0000907e7aaf);
    }
  }
  return;
}


