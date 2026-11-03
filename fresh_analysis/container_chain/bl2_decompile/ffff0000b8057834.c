
int FUN_ffff0000b8057834(long param_1,undefined4 param_2,uint *param_3,int *param_4)

{
  uint uVar1;
  undefined8 uVar2;
  ulong uVar3;
  int iVar4;
  uint *puVar5;
  long lVar6;
  long lVar7;
  uint uVar8;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  puVar5 = (uint *)FUN_ffff0000b804bd14(0x40,0x100);
  if (puVar5 == (uint *)0x0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_ufs_ufs_ffff0000b80895e3,0xf6f,
               s_ufs_temp_buf_ffff0000b8085b07);
  }
  lVar6 = FUN_ffff0000b8009fb4();
  do {
    uVar2 = daif;
    uVar8 = (uint)uVar2;
    if ((uVar8 >> 7 & 1) == 0) {
      uVar3 = daif;
      daif = uVar3 | 0x80;
    }
    uVar1 = 1 << (ulong)(*(byte *)(param_1 + 0x178) & 0x1f);
    if ((*(ulong *)(param_1 + 0x80) & (long)(int)uVar1) == 0) {
      *(undefined8 *)(param_1 + 0x48) = 0;
      *(ulong *)(param_1 + 0x80) = *(ulong *)(param_1 + 0x80) | (long)(int)uVar1;
      if ((uVar8 >> 7 & 1) == 0) {
        uVar3 = daif;
        daif = uVar3 & 0xffffffffffffff7f;
      }
      local_70 = 0;
      uStack_68 = 0;
      local_80 = 0;
      uStack_78 = 0;
      local_90 = 0;
      uStack_88 = 0;
      FUN_ffff0000b80531f4(&local_90,*(byte *)(param_1 + 0x178),param_2,puVar5);
      iVar4 = FUN_ffff0000b8054db0(param_1,&local_90);
      if (iVar4 == 0) {
        uVar2 = daif;
        if (((uint)uVar2 >> 7 & 1) == 0) {
          uVar3 = daif;
          daif = uVar3 | 0x80;
          *(undefined8 *)(param_1 + 0x48) = 0;
          *(ulong *)(param_1 + 0x80) = *(ulong *)(param_1 + 0x80) & (long)(int)~uVar1;
          uVar3 = daif;
          daif = uVar3 & 0xffffffffffffff7f;
        }
        else {
          *(undefined8 *)(param_1 + 0x48) = 0;
          *(ulong *)(param_1 + 0x80) = *(ulong *)(param_1 + 0x80) & (long)(int)~uVar1;
        }
        uVar8 = (*puVar5 & 0xff00ff00) >> 8 | (*puVar5 & 0xff00ff) << 8;
        if (param_4 != (int *)0x0) {
          *param_4 = (uVar8 >> 0x10 | uVar8 << 0x10) + 1;
        }
        uVar8 = (puVar5[1] & 0xff00ff00) >> 8 | (puVar5[1] & 0xff00ff) << 8;
        if (param_3 != (uint *)0x0) {
          *param_3 = uVar8 >> 0x10 | uVar8 << 0x10;
        }
        FUN_ffff0000b804c33c(s__UFS__get_lu_size__lun__d__blk_c_ffff0000b809cf8b,param_2);
      }
      else {
        FUN_ffff0000b804c33c
                  (s__UFS__err___s__ufshcd_queuecomma_ffff0000b8086a5b,
                   s_ufs_get_lu_size_ffff0000b809bf1d);
      }
      thunk_FUN_ffff0000b8082558(puVar5);
      return iVar4;
    }
    *(undefined8 *)(param_1 + 0x48) = 0;
    if ((uVar8 >> 7 & 1) == 0) {
      uVar3 = daif;
      daif = uVar3 & 0xffffffffffffff7f;
    }
    lVar7 = FUN_ffff0000b8009fb4();
  } while ((ulong)(lVar7 - lVar6) < 0x4c4b41);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_common_ufs_ufs_ffff0000b80895e3,0x353,&DAT_ffff0000b8086efb);
}


