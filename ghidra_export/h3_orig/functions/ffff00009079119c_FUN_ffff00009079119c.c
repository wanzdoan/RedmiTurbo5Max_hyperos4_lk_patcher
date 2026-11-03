// ffff00009079119c  size=1184  FUN_ffff00009079119c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009079119c(void)

{
  long lVar1;
  undefined *puVar2;
  long lVar3;
  ulong uVar4;
  int iVar5;
  ulong uVar6;
  undefined8 uVar7;
  ulong uVar8;
  long lVar9;
  ulong uVar10;
  long *plVar11;
  undefined8 *puVar12;
  char local_24 [4];
  
  _DAT_ffff00009090a1f8 = -0xffffe97f0000;
  _DAT_ffff00009090a200 = 0xffff0000168b0000;
  _DAT_ffff00009090a208 = 0xffff000016800000;
  _DAT_ffff00009090a210 = 1;
  _DAT_ffff00009090a218 = FUN_ffff00009077d294(0x80,0xc00);
  if (_DAT_ffff00009090a218 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_ufs_ufs_ffff0000907d6da1,0xb6,
               s_hba_>ucdl_base_addr_ffff0000907d8697);
  }
  _DAT_ffff00009090a228 = FUN_ffff00009077b06c();
  puVar12 = (undefined8 *)&DAT_ffff00009090a218;
  if ((_DAT_ffff00009090a228 & 0x7f) == 0) {
    _DAT_ffff00009090a220 = FUN_ffff00009077d294(0x40,0x40);
    if (_DAT_ffff00009090a220 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_ufs_ufs_ffff0000907d6da1,199,
                 s_hba_>utrdl_base_addr_ffff0000907dedbc);
    }
    _DAT_ffff00009090a230 = FUN_ffff00009077b06c();
    if ((_DAT_ffff00009090a230 & 0x3f) == 0) {
      DAT_ffff00009090a370 = (byte)(_DAT_ffff00009090a220 >> 5) & 0x1f;
      _DAT_ffff00009090a270 = FUN_ffff00009077d24c(0x48);
      uVar4 = _DAT_ffff00009090a228;
      lVar3 = _DAT_ffff00009090a218;
      if (_DAT_ffff00009090a270 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_ufs_ufs_ffff0000907d6da1,0xe2,
                   s_hba_>lrb_ffff0000907fe279);
      }
      uVar10 = (ulong)_DAT_ffff00009090a210;
      if (0 < (int)_DAT_ffff00009090a210) {
        uVar8 = 0;
        plVar11 = (long *)(_DAT_ffff00009090a270 + 0x10);
        lVar9 = _DAT_ffff00009090a220;
        do {
          uVar6 = uVar8 & 0xfffffc00;
          lVar1 = lVar3 + uVar8;
          *(undefined4 *)(lVar9 + 0x18) = 0x800080;
          uVar8 = uVar8 + 0xc00;
          *(undefined2 *)(lVar9 + 0x1e) = 0x100;
          *plVar11 = lVar1 + 0x200;
          plVar11[1] = lVar1 + 0x400;
          *(ulong *)(lVar9 + 0x10) = uVar6 + uVar4;
          plVar11[-2] = lVar9;
          plVar11[-1] = lVar1;
          lVar9 = lVar9 + 0x20;
          plVar11 = plVar11 + 9;
        } while (uVar10 * 0xc00 - uVar8 != 0);
      }
      iVar5 = FUN_ffff00009079163c(&DAT_ffff00009090a1f8);
      if (iVar5 != 0) {
LAB_ffff00009079130c:
        FUN_ffff00009077de54(s__UFS__err__Host_controller_enabl_ffff0000907e0460);
        return -1;
      }
      _DAT_ffff00009090a240 = 0;
      _DAT_ffff00009090a4a8 = *(uint *)(_DAT_ffff00009090a1f8 + 0x2240);
      if ((_DAT_ffff00009090a4a8 | 0x1000000) >> 0x18 == 1) {
        _DAT_ffff00009090a4a8 = _DAT_ffff00009090a4a8 & 0xffffff | 0x10000000;
      }
      iVar5 = FUN_ffff000090791794(&DAT_ffff00009090a1f8);
      if (iVar5 == 0) {
LAB_ffff000090791478:
        uVar7 = FUN_ffff00009075f884();
        _DAT_ffff00009090a4a4 = (int)uVar7;
        if (_DAT_ffff00009090a4a4 == -1) {
          FUN_ffff00009077de54(s__UFS__UFS_IRQ_ID_is_not_define_ffff0000907eb8e4);
        }
        else {
          FUN_ffff0000907272e4(uVar7,1);
          FUN_ffff0000907272d8(_DAT_ffff00009090a4a4,0);
          FUN_ffff000090717388(0xffff00009090a1c0,0,1);
          FUN_ffff000090715fe4(_DAT_ffff00009090a4a4,FUN_ffff00009079441c,0);
          FUN_ffff00009071639c(_DAT_ffff00009090a4a4);
          _DAT_ffff00009090a4a0 = _DAT_ffff00009090a4a0 | 2;
        }
        FUN_ffff000090791094();
        local_24[0] = '\x01';
        iVar5 = FUN_ffff0000907968c8(&DAT_ffff00009090a1f8,2,local_24);
        if (iVar5 == 0) {
          puVar2 = &DAT_ffff0000907ea121;
          if (local_24[0] != '\0') {
            puVar2 = &DAT_ffff0000907df295;
          }
          FUN_ffff00009077de54(s__UFS__iSE_RPMB_key_s_programmed__ffff0000907d2054,puVar2);
          _DAT_ffff00009090a3a4 = 2;
          if (local_24[0] == '\0') {
            _DAT_ffff00009090a3a4 = 3;
          }
        }
        else {
          FUN_ffff00009077de54(s__UFS__iSE_RPMB_key_status_unknow_ffff0000907e8710);
          _DAT_ffff00009090a3a4 = 1;
        }
        local_24[0] = '\x01';
        iVar5 = FUN_ffff0000907968c8(&DAT_ffff00009090a1f8,3,local_24);
        if (iVar5 == 0) {
          puVar2 = &DAT_ffff0000907ea121;
          if (local_24[0] != '\0') {
            puVar2 = &DAT_ffff0000907df295;
          }
          FUN_ffff00009077de54(s__UFS__MTEE_RPMB_key_s_programmed_ffff0000907f37fb,puVar2);
          _DAT_ffff00009090a3a8 = 2;
          if (local_24[0] == '\0') {
            _DAT_ffff00009090a3a8 = 3;
          }
        }
        else {
          FUN_ffff00009077de54(s__UFS__MTEE_RPMB_key_status_unkno_ffff0000907f8e41);
          _DAT_ffff00009090a3a8 = 1;
        }
        return 0;
      }
      if (iVar5 == -1) {
        iVar5 = FUN_ffff00009079163c(&DAT_ffff00009090a1f8);
        if (iVar5 != 0) goto LAB_ffff00009079130c;
        _DAT_ffff00009090a240 = 0;
        _DAT_ffff00009090a4a8 = *(uint *)(_DAT_ffff00009090a1f8 + 0x2240);
        if ((_DAT_ffff00009090a4a8 | 0x1000000) >> 0x18 == 1) {
          _DAT_ffff00009090a4a8 = _DAT_ffff00009090a4a8 & 0xffffff | 0x10000000;
        }
        iVar5 = FUN_ffff000090791794(&DAT_ffff00009090a1f8);
        if (iVar5 == 0) goto LAB_ffff000090791478;
        if (iVar5 == -1) {
          iVar5 = FUN_ffff00009079163c(&DAT_ffff00009090a1f8);
          if (iVar5 != 0) goto LAB_ffff00009079130c;
          _DAT_ffff00009090a240 = 0;
          _DAT_ffff00009090a4a8 = *(uint *)(_DAT_ffff00009090a1f8 + 0x2240);
          if ((_DAT_ffff00009090a4a8 | 0x1000000) >> 0x18 == 1) {
            _DAT_ffff00009090a4a8 = _DAT_ffff00009090a4a8 & 0xffffff | 0x10000000;
          }
          iVar5 = FUN_ffff000090791794(&DAT_ffff00009090a1f8);
          if (iVar5 == 0) goto LAB_ffff000090791478;
          if (iVar5 == -1) {
            iVar5 = FUN_ffff00009079163c(&DAT_ffff00009090a1f8);
            if (iVar5 != 0) goto LAB_ffff00009079130c;
            _DAT_ffff00009090a240 = 0;
            _DAT_ffff00009090a4a8 = *(uint *)(_DAT_ffff00009090a1f8 + 0x2240);
            if ((_DAT_ffff00009090a4a8 | 0x1000000) >> 0x18 == 1) {
              _DAT_ffff00009090a4a8 = _DAT_ffff00009090a4a8 & 0xffffff | 0x10000000;
            }
            iVar5 = FUN_ffff000090791794(&DAT_ffff00009090a1f8);
            if (iVar5 == 0) goto LAB_ffff000090791478;
            if (iVar5 == -1) {
              FUN_ffff00009077de54(s__UFS__err__retry_number_exceeded_ffff0000907d8647);
              return -1;
            }
          }
        }
      }
      FUN_ffff00009077de54(s__UFS__err__ufshcd_probe_hba_fail_ffff0000907ded1a);
      return iVar5;
    }
    puVar12 = (undefined8 *)&DAT_ffff00009090a220;
    thunk_FUN_ffff0000907c2ad8(_DAT_ffff00009090a218);
  }
  thunk_FUN_ffff0000907c2ad8(*puVar12);
  return -1;
}


