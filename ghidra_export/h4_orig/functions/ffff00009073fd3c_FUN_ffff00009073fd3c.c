// ffff00009073fd3c  size=748  FUN_ffff00009073fd3c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009073fd3c(long param_1)

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
  _DAT_ffff00009086f2c0 =
       FUN_ffff0000907198dc(0x10000,0x10000,0xa0000000,0,0,s_SPM_reserved_ffff0000907de355);
  if (_DAT_ffff00009086f2c0 == 0) {
    FUN_ffff00009077e11c(s_mblock_allocation_failed_ffff0000907d47c1);
    return -5;
  }
  local_100 = 0;
  uStack_f8 = 0;
  local_110 = 0;
  uStack_108 = 0;
  iVar2 = FUN_ffff00009077ba64
                    (DAT_ffff000090839928,s_vm_SPM_reserved_ffff0000907d47db,0x10000,
                     &DAT_ffff00009086f2b8,0xc,_DAT_ffff00009086f2c0,0,0);
  if (iVar2 == 0) {
    iVar2 = FUN_ffff000090785d18(&local_f0,s_spmfw_ffff0000907f167b,_DAT_ffff00009086f2b8,0x10000);
    if (iVar2 < 0) {
      FUN_ffff00009077e11c(s__spmfw__load_spmfw_fail__ret_____ffff0000907f666b,iVar2);
      FUN_ffff00009077be30(DAT_ffff000090839928,_DAT_ffff00009086f2b8);
      FUN_ffff00009071a7f0(_DAT_ffff00009086f2c0);
      return iVar2;
    }
    FUN_ffff000090707c70(_DAT_ffff00009086f2b8,0x10000);
    if (param_1 == 0) goto LAB_ffff00009073ff28;
LAB_ffff00009073fe00:
    puVar6 = _DAT_ffff00009086f2b8;
    iVar2 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_sleep_ffff000090802948);
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
          FUN_ffff00009077eb20(local_6c,puVar1,2);
          iVar3 = FUN_ffff00009077f6fc
                            (&local_f0,0x80,&DAT_ffff0000907dbf7c,puVar1 + (ulong)local_6c[0] + 9);
          if (iVar3 < 0) {
            FUN_ffff00009077e11c(s_copy_spmfw_version_string_fail_ffff0000907d47eb);
          }
          else {
            FUN_ffff00009077e11c(s_spmfw_version___s_ffff0000907ee449,&local_f0);
          }
          puVar6 = (uint *)((long)puVar1 + (ulong)puVar6[1]);
        } while ((*puVar6 & 0xfffffffe) == 0x53504d32);
      }
      iVar3 = FUN_ffff00009077f250(&local_f0);
      FUN_ffff00009076e734(param_1,iVar2,s_spmfw_version_ffff0000907eaf94,&local_f0,iVar3 + 1);
      goto LAB_ffff00009073ff70;
    }
    pcVar4 = s__sleep_not_found__LINE___d_ffff0000907dca0e;
    uVar5 = 0x6a;
  }
  else {
    FUN_ffff00009077e11c(s__spmfw__vmm_alloc_physical_faile_ffff0000907dc9eb);
    FUN_ffff00009071a7f0(_DAT_ffff00009086f2c0);
    if (iVar2 < 0) {
      return iVar2;
    }
    if (param_1 != 0) goto LAB_ffff00009073fe00;
LAB_ffff00009073ff28:
    pcVar4 = s_illegal_fdt_address__LINE___d_ffff0000907f9ef1;
    uVar5 = 100;
  }
  FUN_ffff00009077e11c(pcVar4,uVar5);
LAB_ffff00009073ff70:
  FUN_ffff00009077ee50(&local_110,0,0x20);
  FUN_ffff00009071ebf4(0x82000410,0xda065731,1,_DAT_ffff00009086f2c0,0x10000,0,0,0,&local_110);
  FUN_ffff00009077be30(DAT_ffff000090839928,_DAT_ffff00009086f2b8);
  FUN_ffff00009071a7f0(_DAT_ffff00009086f2c0);
  return 0;
}


