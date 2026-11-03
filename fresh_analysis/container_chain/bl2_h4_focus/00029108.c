// File offset entry: 0x29108

int FUN_ffff0000b8029108(void)

{
  uint *puVar1;
  undefined4 *puVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  undefined4 uVar6;
  ulong uVar7;
  char *pcVar8;
  undefined8 *puVar9;
  long lVar10;
  uint *puVar11;
  uint uVar12;
  uint uVar13;
  long local_68;
  
  local_68 = 0;
  uVar7 = FUN_ffff0000b800c9f4(0x500000,0x200000,0xc0000000,0,0,s_tee_secmem_ffff0000b8089ca9);
  iVar5 = FUN_ffff0000b800e3e0();
  if (iVar5 != 0) {
    FUN_ffff0000b800c9f4
              (0x500000,0x200000,0xffffffffffffffff,uVar7 + 0x400000000,0,
               s_tee_secmem_ffff0000b8089ca9);
  }
  if (uVar7 == 0) {
    FUN_ffff0000b804c33c(s___s__mblock_reserve_failed__ffff0000b809f8be,s_TZ_TAGS_ffff0000b80981bb);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_trustzo_ffff0000b809c882,0x13f,&DAT_ffff0000b8086efb);
  }
  DAT_ffff0000b8124f90 = uVar7;
  iVar5 = FUN_ffff0000b804a5f8
                    (PTR_DAT_ffff0000b81086d8,s_vm_tztags_reserved_ffff0000b808fdb0,0x500000,
                     &local_68,0xc,uVar7,0,0);
  lVar4 = local_68;
  uVar7 = DAT_ffff0000b8124f90;
  if (iVar5 != 0) {
    FUN_ffff0000b804c33c(s_vmm_alloc_physical_failed__err____ffff0000b809560c,iVar5);
    return iVar5;
  }
  uVar12 = (uint)DAT_ffff0000b8124f90;
  if (uVar12 == 0) {
    pcVar8 = s___s__secmem_tags_are_not_prepare_ffff0000b809c8af;
  }
  else {
    uVar13 = (uint)(DAT_ffff0000b8124f90 + 0x500000);
    if (uVar13 != uVar12) {
      puVar1 = (uint *)(local_68 + 0x200);
      FUN_ffff0000b804c33c
                (s___s__0x_llx_0x_llx__res__ffff0000b8096515,s_TZ_TAGS_ffff0000b80981bb,
                 DAT_ffff0000b8124f90,DAT_ffff0000b8124f90 + 0x500000);
      if (puVar1 == (uint *)0x0) {
        FUN_ffff0000b804c33c(s___s__param_is_null__ffff0000b8090d21,s_TZ_TAGS_ffff0000b80981bb);
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000b804b830
                  (s_platform_mediatek_common_trustzo_ffff0000b809c882,0x161,&DAT_ffff0000b8086efb);
      }
      FUN_ffff0000b804c33c
                (s___s__tag_start_addr__0x_llx_ffff0000b809e797,s_TZ_TAGS_ffff0000b80981bb,
                 DAT_ffff0000b8124f90 + 0x200);
      FUN_ffff0000b804d070(puVar1,0,0x100);
      uVar3 = uVar13 - 0x401000;
      *(undefined4 *)(lVar4 + 0x21c) = 0x8535399;
      *(uint *)(lVar4 + 0x220) = uVar13;
      *(undefined2 *)(lVar4 + 0x218) = 0x100;
      puVar1[0] = 0x544147;
      puVar1[1] = 0x14535301;
      *(ulong *)(lVar4 + 0x208) = uVar7 & 0xffffffff;
      *(uint *)(lVar4 + 0x22c) = uVar13 - 0x100000;
      *(ulong *)(lVar4 + 0x210) = (ulong)uVar3;
      *(undefined4 *)(lVar4 + 0x224) = 0;
      *(undefined4 *)(lVar4 + 0x228) = 0x8535313;
      *(undefined8 *)(lVar4 + 0x230) = 0x853531200100000;
      *(undefined4 *)(lVar4 + 0x240) = 0x8535314;
      *(uint *)(lVar4 + 0x244) = uVar13 - 0x400000;
      *(uint *)(lVar4 + 0x238) = uVar13 - 0x200000;
      *(undefined4 *)(lVar4 + 0x23c) = 0x100000;
      *(uint *)(lVar4 + 0x250) = uVar3;
      *(undefined4 *)(lVar4 + 0x254) = 0x1000;
      *(undefined4 *)(lVar4 + 0x248) = 0x200000;
      *(undefined4 *)(lVar4 + 0x24c) = 0x8535315;
      if (uVar3 < uVar12) {
        FUN_ffff0000b804c33c
                  (s___s__0x_x_exceeds_0x_x_ffff0000b8090d36,s_TZ_TAGS_ffff0000b80981bb,
                   uVar7 & 0xffffffff);
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000b804b830
                  (s_platform_mediatek_common_trustzo_ffff0000b809c882,0xa2,&DAT_ffff0000b8086efb);
      }
      *(undefined4 *)(lVar4 + 600) = 0x2c535320;
      iVar5 = FUN_ffff0000b801f73c();
      if (iVar5 == 2) {
        uVar6 = FUN_ffff0000b8059e98();
        *(undefined4 *)(lVar4 + 0x27c) = uVar6;
        uVar7 = FUN_ffff0000b8059e74();
        lVar10 = 0x84;
        *(uint *)(lVar4 + 0x280) = (uint)(uVar7 >> 1) & 0x7fffffff;
        uVar6 = FUN_ffff0000b8059ebc();
      }
      else {
        iVar5 = FUN_ffff0000b801f73c();
        if (iVar5 == 1) {
          uVar6 = FUN_ffff0000b80522bc();
          *(undefined4 *)(lVar4 + 0x27c) = uVar6;
          lVar10 = 0x80;
          uVar6 = FUN_ffff0000b80522e0();
        }
        else {
          uVar6 = 0;
          lVar10 = 0x7c;
        }
      }
      *(undefined4 *)((long)puVar1 + lVar10) = uVar6;
      lVar10 = (ulong)*(byte *)(lVar4 + 0x25b) + 0x5c;
      puVar2 = (undefined4 *)((long)puVar1 + lVar10);
      *puVar2 = 0x8535330;
      puVar2[3] = 0x18535331;
      puVar9 = (undefined8 *)FUN_ffff0000b800db20(s_gz_tee_static_shm_ffff0000b8092767,0);
      if (puVar9 != (undefined8 *)0x0) {
        puVar2[8] = (int)*puVar9;
        puVar2[9] = (int)puVar9[1];
      }
      uVar7 = lVar10 + (ulong)*(byte *)((long)puVar2 + 0xf) + 0x10;
      *(undefined4 *)((long)puVar1 + uVar7) = 0x474154;
      if (0xfc < uVar7) {
        FUN_ffff0000b804c33c
                  (s___s__0x_x_exceeds_0x_x_ffff0000b8090d36,s_TZ_TAGS_ffff0000b80981bb,
                   (int)uVar7 + (int)puVar1 + 4,(int)puVar1 + 0x100);
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000b804b830
                  (s_platform_mediatek_common_trustzo_ffff0000b809c882,0x16f,&DAT_ffff0000b8086efb);
      }
      uVar12 = *puVar1;
      puVar11 = puVar1;
      if ((uVar12 & 0xffffff) != 0x544147) {
        FUN_ffff0000b804c33c
                  (s___s__secmem_params_are_not_tag_f_ffff0000b808650d,s_TZ_TAGS_ffff0000b80981bb);
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000b804b830
                  (s_platform_mediatek_common_trustzo_ffff0000b809c882,0xd4,&DAT_ffff0000b8086efb);
      }
      do {
        uVar12 = uVar12 & 0xffffff;
        if (((0x30 < uVar12 - 0x535301) ||
            ((1L << ((ulong)(uVar12 - 0x535301) & 0x3f) & 0x18000801f8001U) == 0)) &&
           (uVar12 != 0x535399)) {
          if (uVar12 == 0x544147) {
            FUN_ffff0000b804c33c
                      (s___s__secmem_is_tag_format_ffff0000b80a04f0,s_TZ_TAGS_ffff0000b80981bb);
          }
          else {
            FUN_ffff0000b804c33c
                      (s___s__unknown_tags__0x_x_ffff0000b8098e9a,s_TZ_TAGS_ffff0000b80981bb);
          }
        }
        puVar11 = (uint *)((long)puVar11 + (ulong)*(byte *)((long)puVar11 + 3) + 4);
        uVar12 = *puVar11;
      } while (((long)puVar11 - (long)puVar1 < 0x100) && ((uVar12 & 0xffffff) != 0x474154));
      FUN_ffff0000b804c33c
                (s___s__remained_tag_len__0x_lx_ffff0000b809e7b4,s_TZ_TAGS_ffff0000b80981bb,
                 (lVar4 - ((long)puVar11 + (ulong)(uVar12 >> 0x18))) + 0x2fc);
      goto LAB_ffff0000b80294a4;
    }
    pcVar8 = s___s__no_space_for_secmem_tags__ffff0000b808fdc3;
  }
  FUN_ffff0000b804c33c(pcVar8,s_TZ_TAGS_ffff0000b80981bb);
LAB_ffff0000b80294a4:
  FUN_ffff0000b804a7a4(PTR_DAT_ffff0000b81086d8,local_68);
  return 0;
}


