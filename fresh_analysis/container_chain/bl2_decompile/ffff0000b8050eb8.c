
uint FUN_ffff0000b8050eb8(undefined8 param_1,undefined8 param_2,long param_3,ulong param_4)

{
  uint uVar1;
  bool bVar2;
  long lVar3;
  int iVar4;
  uint uVar5;
  int iVar6;
  int iVar7;
  uint *puVar8;
  long lVar9;
  char *pcVar10;
  uint uVar11;
  ulong uVar12;
  long lVar13;
  long lVar14;
  undefined8 local_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  
  iVar4 = FUN_ffff0000b800a01c();
  puVar8 = (uint *)FUN_ffff0000b804bccc(0x200);
  if (puVar8 == (uint *)0x0) {
    return 0xfffffff4;
  }
  local_70 = 0;
  uStack_68 = 0;
  local_80 = 0;
  uStack_78 = 0;
  local_90 = 0;
  uStack_88 = 0;
  local_a0 = 0;
  uStack_98 = 0;
  local_a8 = 0;
  FUN_ffff0000b801f7b4(param_1,&local_a8,0x48);
  lVar9 = FUN_ffff0000b804a99c(&local_a8);
  if (lVar9 == 0) {
    FUN_ffff0000b804c33c(s_Partition___s_____s__is_not_exis_ffff0000b80993e8,param_1,&local_a8);
    thunk_FUN_ffff0000b8082558(puVar8);
    return 0xffffffed;
  }
  uVar5 = FUN_ffff0000b804aa30(lVar9,puVar8,0,0x200);
  if ((int)uVar5 < 0x200) {
    FUN_ffff0000b804c33c(s_failed_to_read_hdr___d__ffff0000b808a853,uVar5);
LAB_ffff0000b8051160:
    uVar11 = 0xfffffffb;
  }
  else {
    uVar11 = (*puVar8 & 0xff00ff00) >> 8 | (*puVar8 & 0xff00ff) << 8;
    if ((uVar11 >> 0x10 | uVar11 << 0x10) == 0xd7b7ab1e) {
      uVar5 = (puVar8[1] & 0xff00ff00) >> 8 | (puVar8[1] & 0xff00ff) << 8;
      uVar12 = (ulong)(uVar5 >> 0x10 | uVar5 << 0x10);
      FUN_ffff0000b804c33c(s_DT_hdr_detected___zu__ffff0000b808e4c9,uVar12);
      lVar13 = 0;
      uVar11 = 0x20;
      lVar3 = 0;
joined_r0xffff0000b8051294:
      lVar14 = lVar3;
      if ((param_4 != 0) && (param_4 < uVar12)) {
        FUN_ffff0000b804c33c(s_fatal__buffer_len_<_total_len_ffff0000b808b028);
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000b804b830
                  (s_platform_mediatek_common_loader__ffff0000b808b047,0x86,&DAT_ffff0000b8086efb);
      }
      iVar6 = thunk_FUN_ffff0000b805b018(&local_a8);
      FUN_ffff0000b804c33c(s_img_auth_required____d_ffff0000b809786a,iVar6 != 0);
      if (iVar6 == 0) {
        iVar7 = thunk_FUN_ffff0000b806af68(&local_a8,param_2,puVar8,uVar11,lVar13);
        if (iVar7 != 0) {
          FUN_ffff0000b804c33c(s__SEC___s_cert_parsing_fail__0x_x_ffff0000b8090370,param_2,iVar7);
        }
      }
      else {
        iVar7 = FUN_ffff0000b8052c3c();
        if (iVar7 != 0) {
          FUN_ffff0000b804c33c(s__SEC___s_Cert_Chain_Verify_Fail___ffff0000b80a0988,param_2,iVar7);
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000b804b830
                    (s_platform_mediatek_common_loader__ffff0000b808b047,0x95,&DAT_ffff0000b8086efb)
          ;
        }
        iVar7 = FUN_ffff0000b8052bc4(param_2);
        if (iVar7 != 0) {
          FUN_ffff0000b804c33c(s__SEC__ARB_Check_Fail__0x_x__ffff0000b808b074,iVar7);
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000b804b830
                    (s_platform_mediatek_common_loader__ffff0000b808b047,0x9b,&DAT_ffff0000b8086efb)
          ;
        }
        iVar7 = thunk_FUN_ffff0000b806b29c(puVar8,uVar11);
        if (iVar7 != 0) {
          FUN_ffff0000b804c33c(s__SEC___s_Header_Auth_Fail__0x_x__ffff0000b8097882,param_2,iVar7);
                    /* WARNING: Subroutine does not return */
          FUN_ffff0000b804b830
                    (s_platform_mediatek_common_loader__ffff0000b808b047,0xa1,&DAT_ffff0000b8086efb)
          ;
        }
      }
      uVar5 = FUN_ffff0000b804aa30(lVar9,param_3,lVar14,uVar12);
      if ((long)uVar12 <= (long)(int)uVar5) {
        if (iVar6 == 0) {
          iVar6 = thunk_FUN_ffff0000b806a460(param_3,uVar12);
          if (iVar6 != 0) {
            FUN_ffff0000b804c33c(s__SEC___s_Image_Restore_Fail__0x__ffff0000b808797b,param_2,iVar6);
                    /* WARNING: Subroutine does not return */
            FUN_ffff0000b804b830
                      (s_platform_mediatek_common_loader__ffff0000b808b047,0xcf,
                       &DAT_ffff0000b8086efb);
          }
        }
        else {
          uVar1 = (*puVar8 & 0xff00ff00) >> 8 | (*puVar8 & 0xff00ff) << 8;
          if ((uVar1 >> 0x10 | uVar1 << 0x10) != 0xd7b7ab1e) {
            uVar11 = 0;
          }
          param_3 = param_3 + (ulong)uVar11;
          uVar12 = uVar12 - uVar11;
          iVar6 = thunk_FUN_ffff0000b806b460(param_3,uVar12 & 0xffffffff);
          if (iVar6 != 0) {
            FUN_ffff0000b804c33c(s__SEC___s_Image_Auth_Fail__0x_x__ffff0000b809b086,param_2,iVar6);
                    /* WARNING: Subroutine does not return */
            FUN_ffff0000b804b830
                      (s_platform_mediatek_common_loader__ffff0000b808b047,0xc3,
                       &DAT_ffff0000b8086efb);
          }
        }
        iVar6 = FUN_ffff0000b800a01c();
        FUN_ffff0000b804c33c
                  (s_Load___s__partition_to_0x_p___d_b_ffff0000b809de40,&local_a8,param_3,uVar5,
                   iVar6 - iVar4);
        FUN_ffff0000b8052590(puVar8 + 2,param_3,uVar12);
        bVar2 = false;
        uVar11 = 0;
        goto LAB_ffff0000b8051228;
      }
      pcVar10 = s_failed_to_read___llu___d__ffff0000b809940c;
      uVar12 = (ulong)uVar5;
LAB_ffff0000b805115c:
      FUN_ffff0000b804c33c(pcVar10,lVar14,uVar12);
      goto LAB_ffff0000b8051160;
    }
    if (*puVar8 == 0x58881688) {
      lVar14 = 0;
      do {
        if (*(long *)(lVar9 + 0x20) <= lVar14) goto LAB_ffff0000b8051220;
        if (*puVar8 != 0x58881688) {
          pcVar10 = s_partition_magic_error_ffff0000b80911fe;
          goto LAB_ffff0000b805121c;
        }
        *(undefined1 *)((long)puVar8 + 0x27) = 0;
        iVar6 = FUN_ffff0000b804d510(puVar8 + 2,param_2,0x20);
        if (iVar6 == 0) {
          FUN_ffff0000b804c33c(s__s_image_name_found_ffff0000b809b0a7,param_2);
          uVar12 = (ulong)puVar8[1];
          FUN_ffff0000b804c33c(s_mkimg_hdr_detected___zu__ffff0000b809ee25,uVar12);
          uVar11 = 0x200;
          lVar3 = lVar14 + 0x200;
          lVar13 = lVar14;
          goto joined_r0xffff0000b8051294;
        }
        lVar14 = lVar14 + (ulong)((puVar8[1] + puVar8[0x11]) - 1 & -puVar8[0x11]) + 0x200;
        uVar12 = FUN_ffff0000b804aa30(lVar9,puVar8,lVar14,0x200);
      } while (0x1ff < (int)uVar12);
      pcVar10 = s_failed_to_read_mkimg_hdr___llu___ffff0000b8096f8b;
      goto LAB_ffff0000b805115c;
    }
    pcVar10 = s_non_of_known_hdr_detected_ffff0000b8090355;
LAB_ffff0000b805121c:
    FUN_ffff0000b804c33c(pcVar10);
LAB_ffff0000b8051220:
    uVar11 = 0xffffffea;
  }
  bVar2 = true;
LAB_ffff0000b8051228:
  FUN_ffff0000b804a920(lVar9);
  thunk_FUN_ffff0000b8082558(puVar8);
  if (bVar2) {
    return uVar11;
  }
  return uVar5;
}


