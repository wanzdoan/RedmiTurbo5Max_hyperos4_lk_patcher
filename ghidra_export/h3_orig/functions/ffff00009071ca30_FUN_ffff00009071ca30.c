// ffff00009071ca30  size=1292  FUN_ffff00009071ca30

void FUN_ffff00009071ca30(long param_1)

{
  uint uVar1;
  ulong uVar2;
  bool bVar3;
  undefined4 uVar4;
  int iVar5;
  uint uVar6;
  uint *puVar7;
  long lVar8;
  long *plVar9;
  char *pcVar10;
  ulong *puVar11;
  ulong uVar12;
  uint uVar13;
  ulong uVar14;
  int iVar15;
  ulong local_f0;
  long local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  ulong uStack_c0;
  undefined2 local_b8;
  ulong local_b0 [6];
  ulong local_80;
  long lStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined4 local_44;
  
  local_b8 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  local_e8 = 0;
  uStack_e0 = 0;
  local_f0 = 0;
  uVar4 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_mt6991_adspsys_ffff0000907feb4e);
  iVar5 = FUN_ffff000090781514();
  pcVar10 = s_disabled_ffff0000907f0e0d;
  if (iVar5 != 0) {
    pcVar10 = &DAT_ffff0000907df5a6;
  }
  FUN_ffff00009077f434(&uStack_c0,10,&DAT_ffff0000907dbe71,pcVar10);
  iVar5 = FUN_ffff00009077ef88(&uStack_c0);
  FUN_ffff00009076e40c(param_1,uVar4,s_status_ffff0000907edb6a,&uStack_c0,iVar5 + 1);
  iVar5 = FUN_ffff00009078158c();
  if (iVar5 != 0) {
    uVar13 = 0;
    do {
      FUN_ffff00009077eb88(&uStack_e0,0,0x20);
      FUN_ffff00009077f434
                (&uStack_e0,0x20,s__s__d_ffff0000907ef3c0,
                 s_mediatek_mt6991_adsp_core_ffff0000907e5c74,uVar13);
      uVar4 = FUN_ffff00009076e064(param_1,0xffffffff,&uStack_e0);
      iVar5 = FUN_ffff00009078156c(uVar13);
      if (iVar5 == 0) {
        FUN_ffff00009077f434(&uStack_c0,10,&DAT_ffff0000907dbe71,s_disabled_ffff0000907f0e0d);
        iVar5 = FUN_ffff00009077ef88(&uStack_c0);
        iVar5 = iVar5 + 1;
        puVar11 = &uStack_c0;
        pcVar10 = s_status_ffff0000907edb6a;
      }
      else {
        lVar8 = FUN_ffff0000907815a0(uVar13,2);
        puVar11 = &local_f0;
        pcVar10 = s_system_ffff0000907ed961;
        uVar12 = (*(ulong *)(lVar8 + 0x18) & 0xff00ff00ff00ff00) >> 8 |
                 (*(ulong *)(lVar8 + 0x18) & 0xff00ff00ff00ff) << 8;
        uVar12 = (uVar12 & 0xffff0000ffff0000) >> 0x10 | (uVar12 & 0xffff0000ffff) << 0x10;
        local_f0 = uVar12 >> 0x20 | uVar12 << 0x20;
        iVar5 = 0x10;
        uVar12 = ((ulong)*(uint *)(lVar8 + 0x20) & 0xff00ff00ff00ff00) >> 8 |
                 ((ulong)*(uint *)(lVar8 + 0x20) & 0xff00ff00ff00ff) << 8;
        local_e8 = (uVar12 >> 0x10 | (uVar12 & 0xffff0000ffff) << 0x10) << 0x20;
      }
      FUN_ffff00009076e40c(param_1,uVar4,pcVar10,puVar11,iVar5);
      uVar13 = uVar13 + 1;
      uVar6 = FUN_ffff00009078158c();
    } while (uVar13 < uVar6);
  }
  iVar5 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_snd_audio_dsp_ffff000090801fac);
  if ((-1 < iVar5) && (iVar5 = FUN_ffff00009076d840(param_1,iVar5), -1 < iVar5)) {
    iVar15 = 0;
    uVar13 = 0;
    do {
      local_80 = 0;
      local_b0[0] = local_b0[0] & 0xffffffff00000000;
      puVar7 = (uint *)FUN_ffff00009076dbe8(param_1,iVar5,&local_80,local_b0);
      if ((int)local_b0[0] == 8) {
        uVar6 = (puVar7[1] & 0xff00ff00) >> 8 | (puVar7[1] & 0xff00ff) << 8;
        uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
        bVar3 = ((*puVar7 & 0xff00ff00) >> 8 & iVar15 << 0x10) == 0;
LAB_ffff00009071cc20:
        uVar1 = 0;
        if (!bVar3) {
          uVar1 = uVar6;
        }
        uVar13 = uVar1 + uVar13;
      }
      else if ((int)local_b0[0] == 0x14) {
        bVar3 = *puVar7 == 0;
        uVar6 = (puVar7[4] & 0xff00ff00) >> 8 | (puVar7[4] & 0xff00ff) << 8;
        uVar6 = uVar6 >> 0x10 | uVar6 << 0x10;
        if (!bVar3) {
          iVar15 = 1;
        }
        goto LAB_ffff00009071cc20;
      }
      iVar5 = FUN_ffff00009076d8c4(param_1,iVar5);
    } while (-1 < iVar5);
    if (iVar15 != 0) {
      uVar4 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_mt6991_adspsys_ffff0000907feb4e);
      uVar13 = (uVar13 & 0xff00ff00) >> 8 | (uVar13 & 0xff00ff) << 8;
      local_80 = CONCAT44(local_80._4_4_,uVar13 >> 0x10 | uVar13 << 0x10);
      FUN_ffff00009076f30c(param_1,uVar4,s_adsp_rsv_audio_ffff0000907edb71,&local_80,4);
    }
  }
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cc18(s_kernel_fdt_is_NULL__ffff0000907fcd60);
  }
  local_b0[4] = 0;
  local_b0[5] = 0;
  local_b0[0] = 0;
  local_b0[1] = 0;
  local_b0[2] = 0;
  local_b0[3] = 0;
  uVar4 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_mt6991_adspsys_ffff0000907feb4e);
  iVar5 = FUN_ffff00009076d840(param_1,uVar4);
  if (iVar5 < 0) {
    iVar15 = 0;
  }
  else {
    iVar15 = 0;
    do {
      local_80 = 0;
      local_44 = 0;
      puVar7 = (uint *)FUN_ffff00009076dbe8(param_1,iVar5,&local_80,&local_44);
      lVar8 = FUN_ffff00009077f27c(local_80,s_adsp_rsv_ffff0000907ddada);
      if (lVar8 != 0) {
        uVar13 = (*puVar7 & 0xff00ff00) >> 8 | (*puVar7 & 0xff00ff) << 8;
        iVar15 = ((uVar13 >> 0x10 | uVar13 << 0x10) + 0xfff & 0xfffff000) + iVar15;
      }
      iVar5 = FUN_ffff00009076d8c4(param_1,iVar5);
    } while (-1 < iVar5);
  }
  iVar5 = FUN_ffff00009076e064
                    (param_1,0xffffffff,s_mediatek_reserve_memory_adsp_sha_ffff0000907f7c35);
  if (iVar5 < 0) {
    iVar5 = FUN_ffff00009071c57c();
    uVar12 = 0x23fffffff;
    if (iVar5 != 0) {
      uVar12 = 0x33fffffff;
    }
    plVar9 = (long *)FUN_ffff00009071a204(s_adsp_image_reserved_ffff0000907ef38d,0);
    if ((plVar9 != (long *)0x0) && (lVar8 = *plVar9, lVar8 != 0)) {
      uVar14 = (ulong)(iVar15 + 0xffffU & 0xffff0000);
      uVar2 = lVar8 + 0x80000000U;
      if (uVar12 <= lVar8 + 0x80000000U) {
        uVar2 = uVar12;
      }
      uVar12 = FUN_ffff000090719f58
                         (uVar14,0x2000000,lVar8,uVar2,0,0,s_adsp_shared_reserved_ffff0000907e0ccd);
      if (uVar12 != 0) {
        iVar5 = FUN_ffff000090781514();
        if (iVar5 != 0) {
          local_70 = 0;
          uStack_68 = 0;
          local_80 = 0;
          lStack_78 = 0;
          FUN_ffff00009071ea6c(0xc2000419,0,uVar12,uVar14,0,0,0,0,&local_80);
          if ((int)local_80 < 0) {
            return;
          }
          FUN_ffff00009071ea6c(0xc2000419,6,1,uVar12,uVar14,0,0,0,local_b0);
          if (local_b0[0] != 0) {
            FUN_ffff00009077de54
                      (s__s_fail_CFG_REMAP_SHARED_ret__ld_ffff0000907ea793,&DAT_ffff0000907da691);
          }
        }
        lStack_78 = uVar12 + uVar14;
        local_70 = 0xb6db6c0000001d;
        uStack_68 = 0xb6da2d;
        local_80 = uVar12;
        FUN_ffff00009078ae3c(&local_80);
        uVar12 = (uVar12 & 0xff00ff00ff00ff00) >> 8 | (uVar12 & 0xff00ff00ff00ff) << 8;
        uVar12 = (uVar12 & 0xffff0000ffff0000) >> 0x10 | (uVar12 & 0xffff0000ffff) << 0x10;
        local_b0[4] = uVar12 >> 0x20 | uVar12 << 0x20;
        local_b0[5] = (((uVar14 & 0xff00ff00ff00ff00) >> 8 | (uVar14 & 0xff00ff00ff00ff) << 8) >>
                      0x10) << 0x20;
        FUN_ffff00009076e40c(param_1,uVar4,s_shared_memory_ffff0000907e26e7,local_b0 + 4,0x10);
      }
    }
  }
  else {
    uVar12 = (ulong)(iVar15 + 0xffffU & 0xffff0000);
    local_80 = (((uVar12 & 0xff00ff00ff00ff00) >> 8 | (uVar12 & 0xff00ff00ff00ff) << 8) >> 0x10) <<
               0x20;
    FUN_ffff00009076f30c(param_1,iVar5,&DAT_ffff0000907d582a,&local_80,8);
  }
  return;
}


