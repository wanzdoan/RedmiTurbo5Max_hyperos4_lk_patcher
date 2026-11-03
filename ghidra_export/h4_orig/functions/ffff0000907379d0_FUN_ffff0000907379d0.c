// ffff0000907379d0  size=3912  FUN_ffff0000907379d0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907379d0(undefined8 param_1,long *param_2,long *param_3)

{
  long lVar1;
  long lVar2;
  uint uVar3;
  undefined8 uVar4;
  int iVar5;
  int iVar6;
  int iVar7;
  int iVar8;
  long lVar9;
  long lVar10;
  int iVar11;
  long lVar12;
  ulong uVar13;
  int iVar14;
  undefined8 local_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  iVar5 = FUN_ffff00009078aba4();
  lVar9 = FUN_ffff00009071a348(s_SCP_reserved_ffff0000907f48b4,0);
  _DAT_ffff00001cf30004 = 1;
  _DAT_ffff00001cf40004 = 1;
  _DAT_ffff00001cf21000 = 3;
  lVar12 = *param_3;
  iVar6 = FUN_ffff00009078abb8();
  iVar7 = FUN_ffff000090785d18(param_1,s_tinysys_scp_RV55_A_dram_ffff0000907efb7e,lVar12,0x1100000);
  uVar3 = iVar7 + 0xffffU & 0xffff0000;
  if (((int)uVar3 < iVar6) && (FUN_ffff00009077ee50(lVar12,0,(long)iVar6), uVar3 != 0)) {
    FUN_ffff00009078ac08(uVar3);
    uVar13 = *(ulong *)(lVar9 + 8);
    uVar3 = uVar3 * 2 + 0x700000;
    if (uVar3 < (uint)uVar13) {
      FUN_ffff000090707c70(*param_3,uVar13 & 0xffffffff);
      uVar4 = DAT_ffff000090839928;
      FUN_ffff00009077be30(DAT_ffff000090839928,*param_3);
      FUN_ffff00009077ba64(uVar4,s_SCP_reserved_ffff0000907f48b4,uVar3,param_3,0xc,*param_2,0,0);
      FUN_ffff00009077e11c
                (s__SCP___s__addr___p__addr_phy___p_ffff0000907f8336,
                 s_scp_revise_vmm_region_ffff0000907eade7,*param_3,*param_2);
      FUN_ffff00009077e11c(s__SCP__resize_info__addr_phy___0x_ffff0000907e2da9,*param_2,*param_3);
      FUN_ffff00009077e11c
                (s__SCP__resize_info__old_new_size___ffff0000907d11bd,uVar13 & 0xffffffff,uVar3);
      FUN_ffff00009071a424(*param_2,uVar13 & 0xffffffff,uVar3);
      local_70 = 0;
      uStack_68 = 0;
      local_80 = 0;
      uStack_78 = 0;
      FUN_ffff00009071ebf4(0xc200040f,2,*param_2,uVar3,0,0,0,0,&local_80);
    }
  }
  _DAT_ffff00001cf210c0 = 0;
  _DAT_ffff00001cf210c4 = 0;
  _DAT_ffff00001cf210c8 = 0;
  _DAT_ffff00001cf210cc = 0;
  _DAT_ffff00001cf210d0 = 0;
  _DAT_ffff00001cf210d4 = 0;
  _DAT_ffff00001cf21080 = 0;
  _DAT_ffff00001cf21084 = 0;
  _DAT_ffff00001cf2102c = 0;
  _DAT_ffff00001cf210d8 = 0;
  _DAT_ffff00001cf21088 = 0;
  _DAT_ffff00001cf2108c = 0;
  _DAT_ffff00001cf21090 = 0;
  _DAT_ffff00001cf21094 = 0;
  local_88 = 0;
  iVar6 = FUN_ffff00009077ba64
                    (DAT_ffff000090839928,s_vm_SCP_sram_reserved_ffff0000907fb91a,(long)iVar5,
                     &local_88,0xc,0x1c800000,0,2);
  if (iVar6 == 0) {
    FUN_ffff00009077ee50(local_88,0,(long)iVar5);
    FUN_ffff00009077be30(DAT_ffff000090839928,local_88);
    lVar9 = *param_2;
    lVar12 = *param_3;
    iVar6 = FUN_ffff000090785d18(param_1,s_tinysys_scp_RV55_A_ffff0000907d4619,lVar12,0x700000);
    if (iVar6 < 1) {
      iVar7 = -1;
    }
    else {
      lVar10 = *param_2;
      lVar1 = *param_3 + 0x700000;
      lVar2 = lVar10 + 0x700000;
      iVar7 = FUN_ffff000090785d18
                        (param_1,s_tinysys_scp_RV55_A_dram_ffff0000907efb7e,lVar1,0x1100000);
      iVar11 = (int)lVar2;
      if (iVar7 < 1) {
        iVar14 = 0;
      }
      else {
        uVar3 = iVar7 + 0x3ffU & 0xfffffc00;
        iVar14 = iVar11 + uVar3;
        FUN_ffff00009077eb20(lVar1 + (ulong)uVar3,lVar1,iVar7);
      }
      FUN_ffff00009077eb20(0xffff00001c800000,lVar12,0x2000);
      iVar8 = FUN_ffff00009071c708();
      _DAT_ffff00001c800004 = (uint)lVar9;
      if (iVar8 != 0) {
        _DAT_ffff00001c800004 = (uint)lVar9 | 2;
      }
      _DAT_ffff00001c800010 = iVar6;
      if (iVar5 <= iVar6) {
        _DAT_ffff00001c800010 = iVar5;
      }
      _DAT_ffff00001c800008 = 0x2000;
      _DAT_ffff00001c80000c = (int)lVar10 + 0x2000;
      _DAT_ffff00001c800014 = iVar11;
      _DAT_ffff00001c800018 = iVar7;
      _DAT_ffff00001c80001c = iVar14;
      _DAT_ffff00001c80002c = FUN_ffff00009078abcc();
      local_70 = 0;
      uStack_68 = 0;
      local_80 = 0;
      uStack_78 = 0;
      FUN_ffff00009071ebf4(0xc200040f,1,lVar2,(long)iVar7,0,0,0,0,&local_80);
      FUN_ffff00009071ebf4(0xc200040f,3,0,0,0,0,0,0,&local_80);
      local_70 = 0;
      uStack_68 = 0;
      local_80 = 0;
      uStack_78 = 0;
      uVar13 = thunk_FUN_ffff00009074b22c(0x129);
      FUN_ffff00009077e11c
                (s__s__d___SCP__efuse_info___0x_08x_ffff0000907df9f9,
                 s_verify_load_scp_image_ffff0000907e4a90,0x1ae,uVar13 & 0xffffffff,
                 (uint)uVar13 >> 7 & 0x1f,(uint)uVar13 >> 0xc & 0x1f,uVar13 >> 0x1b & 0x1f);
      FUN_ffff00009071ebf4(0xc200040f,10,uVar13 & 0xffffffff,0,0,0,0,0,&local_80);
      DataSynchronizationBarrier(3,3,0);
      _DAT_ffff00001cf2a080 = 0x33333033;
      _DAT_ffff00001cf2a084 = 3;
      _DAT_ffff00001cf2a088 = 0;
      uVar13 = FUN_ffff00009078abe0();
      if ((uVar13 & 1) != 0) {
        local_70 = 0;
        uStack_68 = 0;
        local_80 = 0;
        uStack_78 = 0;
        FUN_ffff00009071ebf4(0xc200040f,4,0,0,0,0,0,0,&local_80);
        local_70 = 0;
        uStack_68 = 0;
        local_80 = 0;
        uStack_78 = 0;
        FUN_ffff00009071ebf4(0xc200040f,5,0,0,0,0,0,0,&local_80);
      }
      iVar7 = iVar7 + iVar6;
    }
  }
  else {
    FUN_ffff00009077e11c
              (s__SCP___s__error__ret____d_ffff0000907ff14f,s_verify_load_scp_image_ffff0000907e4a90
               ,iVar6);
    iVar7 = -1;
  }
  return iVar7;
}


