
int FUN_ffff00009079145c(void)

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
  
  DAT_ffff00009090a2b8 = -0xffffe97f0000;
  DAT_ffff00009090a2c0 = 0xffff0000168b0000;
  DAT_ffff00009090a2c8 = 0xffff000016800000;
  DAT_ffff00009090a2d0 = 1;
  DAT_ffff00009090a2d8 = FUN_ffff00009077d55c(0x80,0xc00);
  if (DAT_ffff00009090a2d8 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_ufs_ufs_ffff0000907d6e37,0xb6,
               s_hba_>ucdl_base_addr_ffff0000907d872d);
  }
  DAT_ffff00009090a2e8 = FUN_ffff00009077b334();
  puVar12 = &DAT_ffff00009090a2d8;
  if ((DAT_ffff00009090a2e8 & 0x7f) == 0) {
    DAT_ffff00009090a2e0 = FUN_ffff00009077d55c(0x40,0x40);
    if (DAT_ffff00009090a2e0 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_ufs_ufs_ffff0000907d6e37,199,
                 s_hba_>utrdl_base_addr_ffff0000907dee5f);
    }
    DAT_ffff00009090a2f0 = FUN_ffff00009077b334();
    if ((DAT_ffff00009090a2f0 & 0x3f) == 0) {
      DAT_ffff00009090a430 = (byte)((uint)DAT_ffff00009090a2e0 >> 5) & 0x1f;
      DAT_ffff00009090a330 = FUN_ffff00009077d514(0x48);
      uVar4 = DAT_ffff00009090a2e8;
      lVar3 = DAT_ffff00009090a2d8;
      if (DAT_ffff00009090a330 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_ufs_ufs_ffff0000907d6e37,0xe2,
                   s_hba_>lrb_ffff0000907fe421);
      }
      uVar10 = (ulong)DAT_ffff00009090a2d0;
      if (0 < (int)DAT_ffff00009090a2d0) {
        uVar8 = 0;
        plVar11 = (long *)(DAT_ffff00009090a330 + 0x10);
        lVar9 = DAT_ffff00009090a2e0;
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
      iVar5 = FUN_ffff0000907918fc(&DAT_ffff00009090a2b8);
      if (iVar5 != 0) {
LAB_ffff0000907915cc:
        FUN_ffff00009077e11c(s__UFS__err__Host_controller_enabl_ffff0000907e0503);
        return -1;
      }
      DAT_ffff00009090a300 = 0;
      DAT_ffff00009090a568 = *(uint *)(DAT_ffff00009090a2b8 + 0x2240);
      if ((DAT_ffff00009090a568 | 0x1000000) >> 0x18 == 1) {
        DAT_ffff00009090a568 = DAT_ffff00009090a568 & 0xffffff | 0x10000000;
      }
      iVar5 = FUN_ffff000090791a54(&DAT_ffff00009090a2b8);
      if (iVar5 == 0) {
LAB_ffff000090791738:
        uVar7 = FUN_ffff00009075fb1c();
        DAT_ffff00009090a564 = (int)uVar7;
        if (DAT_ffff00009090a564 == -1) {
          FUN_ffff00009077e11c(s__UFS__UFS_IRQ_ID_is_not_define_ffff0000907eba67);
        }
        else {
          FUN_ffff000090727528(uVar7,1);
          FUN_ffff00009072751c(DAT_ffff00009090a564,0);
          FUN_ffff0000907174cc(&DAT_ffff00009090a280,0,1);
          FUN_ffff000090716128(DAT_ffff00009090a564,FUN_ffff0000907946dc,0);
          FUN_ffff0000907164e0(DAT_ffff00009090a564);
          DAT_ffff00009090a560 = DAT_ffff00009090a560 | 2;
        }
        FUN_ffff000090791354();
        local_24[0] = '\x01';
        iVar5 = FUN_ffff000090796b88(&DAT_ffff00009090a2b8,2,local_24);
        if (iVar5 == 0) {
          puVar2 = &DAT_ffff0000907ea265;
          if (local_24[0] != '\0') {
            puVar2 = &DAT_ffff0000907df338;
          }
          FUN_ffff00009077e11c(s__UFS__iSE_RPMB_key_s_programmed__ffff0000907d2091,puVar2);
          DAT_ffff00009090a464 = 2;
          if (local_24[0] == '\0') {
            DAT_ffff00009090a464 = 3;
          }
        }
        else {
          FUN_ffff00009077e11c(s__UFS__iSE_RPMB_key_status_unknow_ffff0000907e8854);
          DAT_ffff00009090a464 = 1;
        }
        local_24[0] = '\x01';
        iVar5 = FUN_ffff000090796b88(&DAT_ffff00009090a2b8,3,local_24);
        if (iVar5 == 0) {
          puVar2 = &DAT_ffff0000907ea265;
          if (local_24[0] != '\0') {
            puVar2 = &DAT_ffff0000907df338;
          }
          FUN_ffff00009077e11c(s__UFS__MTEE_RPMB_key_s_programmed_ffff0000907f3987,puVar2);
          DAT_ffff00009090a468 = 2;
          if (local_24[0] == '\0') {
            DAT_ffff00009090a468 = 3;
          }
        }
        else {
          FUN_ffff00009077e11c(s__UFS__MTEE_RPMB_key_status_unkno_ffff0000907f8fdd);
          DAT_ffff00009090a468 = 1;
        }
        return 0;
      }
      if (iVar5 == -1) {
        iVar5 = FUN_ffff0000907918fc(&DAT_ffff00009090a2b8);
        if (iVar5 != 0) goto LAB_ffff0000907915cc;
        DAT_ffff00009090a300 = 0;
        DAT_ffff00009090a568 = *(uint *)(DAT_ffff00009090a2b8 + 0x2240);
        if ((DAT_ffff00009090a568 | 0x1000000) >> 0x18 == 1) {
          DAT_ffff00009090a568 = DAT_ffff00009090a568 & 0xffffff | 0x10000000;
        }
        iVar5 = FUN_ffff000090791a54(&DAT_ffff00009090a2b8);
        if (iVar5 == 0) goto LAB_ffff000090791738;
        if (iVar5 == -1) {
          iVar5 = FUN_ffff0000907918fc(&DAT_ffff00009090a2b8);
          if (iVar5 != 0) goto LAB_ffff0000907915cc;
          DAT_ffff00009090a300 = 0;
          DAT_ffff00009090a568 = *(uint *)(DAT_ffff00009090a2b8 + 0x2240);
          if ((DAT_ffff00009090a568 | 0x1000000) >> 0x18 == 1) {
            DAT_ffff00009090a568 = DAT_ffff00009090a568 & 0xffffff | 0x10000000;
          }
          iVar5 = FUN_ffff000090791a54(&DAT_ffff00009090a2b8);
          if (iVar5 == 0) goto LAB_ffff000090791738;
          if (iVar5 == -1) {
            iVar5 = FUN_ffff0000907918fc(&DAT_ffff00009090a2b8);
            if (iVar5 != 0) goto LAB_ffff0000907915cc;
            DAT_ffff00009090a300 = 0;
            DAT_ffff00009090a568 = *(uint *)(DAT_ffff00009090a2b8 + 0x2240);
            if ((DAT_ffff00009090a568 | 0x1000000) >> 0x18 == 1) {
              DAT_ffff00009090a568 = DAT_ffff00009090a568 & 0xffffff | 0x10000000;
            }
            iVar5 = FUN_ffff000090791a54(&DAT_ffff00009090a2b8);
            if (iVar5 == 0) goto LAB_ffff000090791738;
            if (iVar5 == -1) {
              FUN_ffff00009077e11c(s__UFS__err__retry_number_exceeded_ffff0000907d86dd);
              return -1;
            }
          }
        }
      }
      FUN_ffff00009077e11c(s__UFS__err__ufshcd_probe_hba_fail_ffff0000907dedbd);
      return iVar5;
    }
    puVar12 = &DAT_ffff00009090a2e0;
    thunk_FUN_ffff0000907c314c(DAT_ffff00009090a2d8);
  }
  thunk_FUN_ffff0000907c314c(*puVar12);
  return -1;
}


