// ffff00009075c958  size=568  FUN_ffff00009075c958

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075c958(undefined8 param_1)

{
  uint uVar1;
  undefined8 uVar2;
  undefined4 uVar3;
  int iVar4;
  long lVar5;
  long lVar6;
  undefined1 *puVar7;
  undefined8 local_280;
  undefined4 local_278;
  int local_274;
  undefined4 local_270;
  undefined1 auStack_26c [492];
  undefined8 local_80;
  undefined4 local_78;
  uint uStack_74;
  undefined8 local_70;
  undefined8 uStack_68;
  
  lVar5 = FUN_ffff00009075ce0c();
  local_280 = _DAT_ffff00009087bf40;
  local_278 = _DAT_ffff00009087be90;
  if (_DAT_ffff00009087bf38 == 0) {
    _DAT_ffff00009087bf38 = FUN_ffff00009075df90();
    FUN_ffff00009077e11c
              (s__s__fps__d_ffff0000907ee97c,s_mt_disp_get_lcd_time_ffff0000907d34fc,
               _DAT_ffff00009087bf38);
    if (_DAT_ffff00009087bf38 == 0) {
      _DAT_ffff00009087bf38 = 6000;
    }
  }
  iVar4 = _DAT_ffff00009087bf38;
  FUN_ffff00009077ee50(&local_270,0,0x1f0);
  local_274 = iVar4;
  local_270 = FUN_ffff00009075e390();
  puVar7 = auStack_26c;
  if (lVar5 != 0) {
    lVar6 = FUN_ffff00009077f250(lVar5);
    FUN_ffff00009077f2ac(puVar7,lVar5,lVar6 + 1);
    lVar6 = FUN_ffff00009077f250(lVar5);
    puVar7 = puVar7 + (lVar6 + 5U & 0xfffffffffffffffc);
  }
  FUN_ffff00009077e11c(s_videolfb___fb_base___0x_llx_ffff0000907d1962,_DAT_ffff00009087bf40);
  FUN_ffff00009077e11c(s_videolfb___islcmfound____x_ffff0000907ee988,_DAT_ffff00009087be90);
  if (_DAT_ffff00009087bf38 == 0) {
    _DAT_ffff00009087bf38 = FUN_ffff00009075df90();
    FUN_ffff00009077e11c
              (s__s__fps__d_ffff0000907ee97c,s_mt_disp_get_lcd_time_ffff0000907d34fc,
               _DAT_ffff00009087bf38);
    if (_DAT_ffff00009087bf38 == 0) {
      _DAT_ffff00009087bf38 = 6000;
    }
  }
  FUN_ffff00009077e11c(s_videolfb___fps____d_ffff0000907de831);
  uVar3 = FUN_ffff00009075e390();
  FUN_ffff00009077e11c(s_videolfb___vram____d_ffff0000907dced0,uVar3);
  FUN_ffff00009077e11c(s_videolfb___lcmname____s_ffff0000907ee9a4,lVar5);
  uVar3 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  FUN_ffff00009076e6d4
            (param_1,uVar3,s_atag_videolfb_ffff0000907d8161,&local_280,(int)puVar7 - (int)&local_280
            );
  uVar2 = _DAT_ffff00009087bf40;
  iVar4 = FUN_ffff00009076da24(param_1,s__soc_iommu_30800000_iommu_groups_ffff0000907fde63);
  if (iVar4 < 0) {
    FUN_ffff00009077e11c(s__s_get_fail_ffff000090801349,s_set_mm_smmu_address_ffff000090802c4f);
  }
  else {
    uVar1 = ((uint)uVar2 & 0xff00ff00) >> 8 | ((uint)uVar2 & 0xff00ff) << 8;
    uStack_74 = uVar1 >> 0x10 | uVar1 << 0x10;
    local_80 = 0;
    local_78 = 0;
    local_70 = 0xf0ffff00000000;
    uStack_68 = 0x10003000000;
    iVar4 = FUN_ffff00009076e6d4
                      (param_1,iVar4,s_mtk_iommu_dma_range_ffff0000907d4d0e,&local_80,0x20);
    if (iVar4 < 0) {
      FUN_ffff00009077e11c(s_mtk_iommu_dma_range_setprop_fail_ffff0000907fc076,iVar4);
    }
  }
  return;
}


