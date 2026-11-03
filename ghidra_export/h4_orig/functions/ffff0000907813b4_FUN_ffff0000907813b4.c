// ffff0000907813b4  size=1064  FUN_ffff0000907813b4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907813b4(long param_1)

{
  undefined *puVar1;
  uint uVar2;
  uint uVar3;
  undefined4 uVar4;
  int iVar5;
  long lVar6;
  uint *puVar7;
  ulong uVar8;
  ulong uVar9;
  ulong *puVar10;
  undefined4 local_9c;
  undefined8 local_98;
  undefined8 local_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  uint local_64;
  
  local_98 = 0;
  local_90 = 0;
  local_9c = 0;
  if (param_1 != 0) {
    local_64 = 0;
    local_88 = 0;
    uStack_80 = 0;
    local_78 = 0;
    uStack_70 = 0;
    uVar4 = FUN_ffff00009076e32c(param_1,0xffffffff,s_mediatek_mt6991_adspsys_ffff0000907fecf6);
    lVar6 = FUN_ffff00009076df9c(param_1,uVar4,s_status_ffff0000907edcdb,&local_64);
    if (lVar6 == 0) {
      _DAT_ffff00009087cfe0 = 0;
    }
    else {
      iVar5 = FUN_ffff00009077f348(lVar6,&DAT_ffff0000907df649,(long)(int)local_64);
      _DAT_ffff00009087cfe0 = (uint)(iVar5 == 0);
    }
    puVar7 = (uint *)FUN_ffff00009076df9c(param_1,uVar4,s_core_num_ffff0000907d6c17,&local_64);
    if (puVar7 == (uint *)0x0) {
      iVar5 = -0x10;
      _DAT_ffff00009087cfe4 = 0;
      goto code_r0xffff000090781730;
    }
    uVar2 = *puVar7;
    uVar3 = (uVar2 & 0xff00ff00) >> 8 | (uVar2 & 0xff00ff) << 8;
    _DAT_ffff00009087cfe4 = uVar3 >> 0x10 | uVar3 << 0x10;
    if (uVar2 != 0) {
      uVar9 = 0;
      puVar10 = (ulong *)&DAT_ffff00009087d030;
      do {
        *(int *)(puVar10 + -9) = (int)uVar9;
        FUN_ffff00009077ee50(&local_88,0,0x20);
        FUN_ffff00009077f6fc
                  (&local_88,0x20,s__s__d_ffff0000907ef531,
                   s_mediatek_mt6991_adsp_core_ffff0000907e5d88,uVar9 & 0xffffffff);
        uVar4 = FUN_ffff00009076e32c(param_1,0xffffffff,&local_88);
        lVar6 = FUN_ffff00009076df9c(param_1,uVar4,s_status_ffff0000907edcdb,&local_64);
        if ((lVar6 == 0) ||
           (iVar5 = FUN_ffff00009077f348(lVar6,&DAT_ffff0000907df649,(long)(int)local_64),
           iVar5 != 0)) {
          *(undefined4 *)((long)puVar10 + -0x44) = 0;
        }
        else {
          *(undefined4 *)((long)puVar10 + -0x44) = 1;
        }
        lVar6 = FUN_ffff00009076df9c(param_1,uVar4,&DAT_ffff0000907df645,&local_64);
        if ((0 < (int)local_64) && (lVar6 != 0)) {
          puVar1 = &DAT_ffff0000907f0cfa;
          if (uVar9 != 0) {
            puVar1 = &DAT_ffff0000907d3e3d;
          }
          local_64 = local_64 >> 2;
          FUN_ffff00009077f6fc
                    (puVar10 + -8,0x14,s__s__s__s_ffff0000907dec2a,s_hifi5_ffff0000907d9d9c,puVar1,
                     &DAT_ffff0000907d5122);
          FUN_ffff00009077f6fc
                    (puVar10 + -3,0x14,s__s__s__s_ffff0000907dec2a,s_hifi5_ffff0000907d9d9c,puVar1,
                     &DAT_ffff0000907d724c);
          uVar2 = (*(uint *)(lVar6 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar6 + 4) & 0xff00ff) << 8;
          puVar10[-5] = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
          uVar2 = (*(uint *)(lVar6 + 0xc) & 0xff00ff00) >> 8 |
                  (*(uint *)(lVar6 + 0xc) & 0xff00ff) << 8;
          *(uint *)(puVar10 + -4) = uVar2 >> 0x10 | uVar2 << 0x10;
          uVar2 = (*(uint *)(lVar6 + 0x14) & 0xff00ff00) >> 8 |
                  (*(uint *)(lVar6 + 0x14) & 0xff00ff) << 8;
          *puVar10 = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
          uVar2 = (*(uint *)(lVar6 + 0x1c) & 0xff00ff00) >> 8 |
                  (*(uint *)(lVar6 + 0x1c) & 0xff00ff) << 8;
          *(uint *)(puVar10 + 1) = uVar2 >> 0x10 | uVar2 << 0x10;
        }
        lVar6 = FUN_ffff00009076df9c(param_1,uVar4,s_system_ffff0000907edad2,&local_64);
        puVar1 = &DAT_ffff0000907f0cfa;
        if (uVar9 != 0) {
          puVar1 = &DAT_ffff0000907d3e3d;
        }
        FUN_ffff00009077f6fc
                  (puVar10 + 2,0x14,s__s__s__s_ffff0000907dec2a,s_hifi5_ffff0000907d9d9c,puVar1,
                   &DAT_ffff0000907ef255);
        uVar9 = uVar9 + 1;
        uVar8 = (ulong)_DAT_ffff00009087cfe4;
        uVar2 = (*(uint *)(lVar6 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar6 + 4) & 0xff00ff) << 8;
        puVar10[5] = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10);
        uVar2 = (*(uint *)(lVar6 + 0xc) & 0xff00ff00) >> 8 |
                (*(uint *)(lVar6 + 0xc) & 0xff00ff) << 8;
        *(uint *)(puVar10 + 6) = uVar2 >> 0x10 | uVar2 << 0x10;
        puVar10 = puVar10 + 0x10;
      } while (uVar9 < uVar8);
    }
  }
  if (_DAT_ffff00009087cfe0 == 0) {
    iVar5 = -0x10;
  }
  else {
    iVar5 = FUN_ffff00009071c738(&local_90,&local_9c);
    if (iVar5 == -5) {
      iVar5 = -5;
    }
    else {
      FUN_ffff00009071c898(local_90);
      iVar5 = FUN_ffff00009077ba64
                        (DAT_ffff000090839928,s_adsp_image_reserved_ffff0000907ef4fe,local_9c,
                         &local_98,0xc,local_90,0,0);
      if (iVar5 == 0) {
        iVar5 = FUN_ffff00009071c90c(s_audio_dsp_ffff0000907db821,local_90,local_98);
        if (-1 < iVar5) {
          FUN_ffff000090707c70(local_98,local_9c);
          FUN_ffff00009071cb74(0x1c,local_90,local_9c);
          return 0;
        }
        FUN_ffff00009077be30(DAT_ffff000090839928,local_98);
      }
      else {
        iVar5 = -5;
      }
      FUN_ffff00009071a7f0(local_90);
    }
  }
code_r0xffff000090781730:
  _DAT_ffff00009087cfe0 = 0;
  if ((_DAT_ffff00009087cfe4 - 1 < 2) && (_DAT_ffff00009087cfec = 0, _DAT_ffff00009087cfe4 != 1)) {
    _DAT_ffff00009087d06c = 0;
  }
  return iVar5;
}


