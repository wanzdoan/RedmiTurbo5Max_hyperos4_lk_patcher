// ffff00009073faa4  size=748  FUN_ffff00009073faa4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009073faa4(long param_1)

{
  uint *puVar1;
  int iVar2;
  int iVar3;
  char *pcVar4;
  undefined8 uVar5;
  uint *puVar6;
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
  ushort local_6c [6];
  
  local_f0 = 0x77666d7073;
  uStack_e8 = 0;
  _DAT_ffff00009086f228 =
       FUN_ffff000090719798(0x10000,0x10000,0xa0000000,0,0,s_SPM_reserved_ffff0000907de2b2);
  if (_DAT_ffff00009086f228 == 0) {
    FUN_ffff00009077de54(s_mblock_allocation_failed_ffff0000907d4784);
    return -5;
  }
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  iVar2 = FUN_ffff00009077b79c
                    (DAT_ffff000090839908,s_vm_SPM_reserved_ffff0000907d479e,0x10000,
                     &DAT_ffff00009086f220,0xc,_DAT_ffff00009086f228,0,0);
  if (iVar2 == 0) {
    iVar2 = FUN_ffff000090785a50(&local_f0,s_spmfw_ffff0000907f150a,_DAT_ffff00009086f220,0x10000);
    if (iVar2 < 0) {
      FUN_ffff00009077de54(s__spmfw__load_spmfw_fail__ret_____ffff0000907f64e1,iVar2);
      FUN_ffff00009077bb68(DAT_ffff000090839908,_DAT_ffff00009086f220);
      FUN_ffff00009071a6ac(_DAT_ffff00009086f228);
      return iVar2;
    }
    FUN_ffff000090707ce4(_DAT_ffff00009086f220,0x10000);
    if (param_1 == 0) goto LAB_ffff00009073fc90;
LAB_ffff00009073fb68:
    puVar6 = _DAT_ffff00009086f220;
    iVar2 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_sleep_ffff000090802744);
    if (-1 < iVar2) {
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
      if (*puVar6 >> 1 == 0x29a82699) {
        do {
          puVar1 = puVar6 + 0x10;
          local_6c[0] = 0;
          FUN_ffff00009077e858(local_6c,puVar1,2);
          iVar3 = FUN_ffff00009077f434
                            (&local_f0,0x80,&DAT_ffff0000907dbe71,puVar1 + (ulong)local_6c[0] + 9);
          if (iVar3 < 0) {
            FUN_ffff00009077de54(s_copy_spmfw_version_string_fail_ffff0000907d47ae);
          }
          else {
            FUN_ffff00009077de54(s_spmfw_version___s_ffff0000907ee2d8,&local_f0);
          }
          puVar6 = (uint *)((long)puVar1 + (ulong)puVar6[1]);
        } while ((*puVar6 & 0xfffffffe) == 0x53504d32);
      }
      iVar3 = FUN_ffff00009077ef88(&local_f0);
      FUN_ffff00009076e46c(param_1,iVar2,s_spmfw_version_ffff0000907eae11,&local_f0,iVar3 + 1);
      goto LAB_ffff00009073fcd8;
    }
    pcVar4 = s__sleep_not_found__LINE___d_ffff0000907dc938;
    uVar5 = 0x6a;
  }
  else {
    FUN_ffff00009077de54(s__spmfw__vmm_alloc_physical_faile_ffff0000907dc915);
    FUN_ffff00009071a6ac(_DAT_ffff00009086f228);
    if (iVar2 < 0) {
      return iVar2;
    }
    if (param_1 != 0) goto LAB_ffff00009073fb68;
LAB_ffff00009073fc90:
    pcVar4 = s_illegal_fdt_address__LINE___d_ffff0000907f9d55;
    uVar5 = 100;
  }
  FUN_ffff00009077de54(pcVar4,uVar5);
LAB_ffff00009073fcd8:
  FUN_ffff00009077eb88(&local_110,0,0x20);
  FUN_ffff00009071ea6c(0x82000410,0xda065731,1,_DAT_ffff00009086f228,0x10000,0,0,0,&local_110);
  FUN_ffff00009077bb68(DAT_ffff000090839908,_DAT_ffff00009086f220);
  FUN_ffff00009071a6ac(_DAT_ffff00009086f228);
  return 0;
}


