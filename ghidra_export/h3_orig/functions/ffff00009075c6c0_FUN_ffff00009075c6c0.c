// ffff00009075c6c0  size=568  FUN_ffff00009075c6c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075c6c0(undefined8 param_1)

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
  
  lVar5 = FUN_ffff00009075cb74();
  local_280 = _DAT_ffff00009087bea8;
  local_278 = _DAT_ffff00009087bdf8;
  if (_DAT_ffff00009087bea0 == 0) {
    _DAT_ffff00009087bea0 = FUN_ffff00009075dcf8();
    FUN_ffff00009077de54
              (s__s__fps__d_ffff0000907ee80b,s_mt_disp_get_lcd_time_ffff0000907d34bf,
               _DAT_ffff00009087bea0);
    if (_DAT_ffff00009087bea0 == 0) {
      _DAT_ffff00009087bea0 = 6000;
    }
  }
  iVar4 = _DAT_ffff00009087bea0;
  FUN_ffff00009077eb88(&local_270,0,0x1f0);
  local_274 = iVar4;
  local_270 = FUN_ffff00009075e0f8();
  puVar7 = auStack_26c;
  if (lVar5 != 0) {
    lVar6 = FUN_ffff00009077ef88(lVar5);
    FUN_ffff00009077efe4(puVar7,lVar5,lVar6 + 1);
    lVar6 = FUN_ffff00009077ef88(lVar5);
    puVar7 = puVar7 + (lVar6 + 5U & 0xfffffffffffffffc);
  }
  FUN_ffff00009077de54(s_videolfb___fb_base___0x_llx_ffff0000907d1925,_DAT_ffff00009087bea8);
  FUN_ffff00009077de54(s_videolfb___islcmfound____x_ffff0000907ee817,_DAT_ffff00009087bdf8);
  if (_DAT_ffff00009087bea0 == 0) {
    _DAT_ffff00009087bea0 = FUN_ffff00009075dcf8();
    FUN_ffff00009077de54
              (s__s__fps__d_ffff0000907ee80b,s_mt_disp_get_lcd_time_ffff0000907d34bf,
               _DAT_ffff00009087bea0);
    if (_DAT_ffff00009087bea0 == 0) {
      _DAT_ffff00009087bea0 = 6000;
    }
  }
  FUN_ffff00009077de54(s_videolfb___fps____d_ffff0000907de78e);
  uVar3 = FUN_ffff00009075e0f8();
  FUN_ffff00009077de54(s_videolfb___vram____d_ffff0000907dcdfa,uVar3);
  FUN_ffff00009077de54(s_videolfb___lcmname____s_ffff0000907ee833,lVar5);
  uVar3 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea5b1);
  FUN_ffff00009076e40c
            (param_1,uVar3,s_atag_videolfb_ffff0000907d80cb,&local_280,(int)puVar7 - (int)&local_280
            );
  uVar2 = _DAT_ffff00009087bea8;
  iVar4 = FUN_ffff00009076d75c(param_1,s__soc_iommu_30800000_iommu_groups_ffff0000907fdcbb);
  if (iVar4 < 0) {
    FUN_ffff00009077de54(s__s_get_fail_ffff00009080116d,s_set_mm_smmu_address_ffff000090802a4b);
  }
  else {
    uVar1 = ((uint)uVar2 & 0xff00ff00) >> 8 | ((uint)uVar2 & 0xff00ff) << 8;
    uStack_74 = uVar1 >> 0x10 | uVar1 << 0x10;
    local_80 = 0;
    local_78 = 0;
    local_70 = 0xf0ffff00000000;
    uStack_68 = 0x10003000000;
    iVar4 = FUN_ffff00009076e40c
                      (param_1,iVar4,s_mtk_iommu_dma_range_ffff0000907d4cd1,&local_80,0x20);
    if (iVar4 < 0) {
      FUN_ffff00009077de54(s_mtk_iommu_dma_range_setprop_fail_ffff0000907fbeda,iVar4);
    }
  }
  return;
}


