// ffff0000907bdb54  size=1644  FUN_ffff0000907bdb54

undefined4
FUN_ffff0000907bdb54
          (long param_1,long param_2,long param_3,long param_4,long param_5,long param_6,
          long param_7,long param_8)

{
  int iVar1;
  undefined4 uVar2;
  int iVar3;
  ulong uVar4;
  uint *puVar5;
  uint *puVar6;
  long lVar7;
  long lVar8;
  undefined8 uVar9;
  undefined *puVar10;
  char *pcVar11;
  uint uVar12;
  long lVar13;
  long lVar14;
  uint *puVar15;
  long lVar16;
  long lVar17;
  ulong uVar18;
  undefined8 local_68;
  
  if ((((param_1 == 0) || (param_3 == 0)) || (param_5 == 0)) || (param_7 == 0)) {
    uVar9 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_rsa_c_ffff000090800142);
    puVar10 = &DAT_ffff0000907f3d14;
    pcVar11 = s_Invalid_input__ffff0000907f9196;
    goto LAB_ffff0000907be084;
  }
  local_68 = 0;
  uVar4 = FUN_ffff0000907bc1f0(param_1,&local_68);
  if ((uVar4 & 1) == 0) {
    uVar9 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_rsa_c_ffff000090800142);
    puVar10 = &DAT_ffff0000907fc9c6;
    pcVar11 = s_Invalid_key__ffff0000907f774e;
LAB_ffff0000907be050:
    FUN_ffff0000907c0ee8(uVar9,&DAT_ffff0000907ebfa6,puVar10,s___ERROR__ffff0000907e07dc,pcVar11,0);
  }
  else {
    uVar12 = (uint)local_68;
    if ((((uint)local_68 != 0x800) && ((uint)local_68 != 0x1000)) && ((uint)local_68 != 0x2000)) {
      uVar9 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_rsa_c_ffff000090800142);
      puVar10 = &DAT_ffff0000907ef1e9;
      pcVar11 = s_Unexpected_key_length__ffff0000907f5b01;
      goto LAB_ffff0000907be050;
    }
    if ((ulong)((uint)local_68 >> 2) + 8 != param_2) {
      uVar9 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_rsa_c_ffff000090800142);
      puVar10 = &DAT_ffff0000907f775c;
      pcVar11 = s_Key_does_not_match_expected_leng_ffff0000907e59d9;
      goto LAB_ffff0000907be050;
    }
    puVar5 = (uint *)FUN_ffff0000907c1544((ulong)((uint)local_68 >> 2) + 0x18);
    if (puVar5 != (uint *)0x0) {
      uVar4 = (ulong)((uint)local_68 >> 5);
      *puVar5 = (uint)local_68 >> 5;
      puVar5[1] = local_68._4_4_;
      *(uint **)(puVar5 + 2) = puVar5 + 6;
      *(uint **)(puVar5 + 4) = puVar5 + 6 + uVar4;
      if (0x1f < (uint)local_68) {
        uVar18 = 0;
        iVar3 = -1;
        do {
          uVar2 = FUN_ffff0000907c1050
                            (*(undefined4 *)(param_1 + 8 + (ulong)(uint)((int)uVar4 + iVar3) * 4));
          lVar17 = uVar18 * 4;
          *(undefined4 *)(*(long *)(puVar5 + 2) + lVar17) = uVar2;
          uVar2 = FUN_ffff0000907c1050
                            (*(undefined4 *)
                              (param_1 + 8 + (ulong)(uVar12 >> 3) + (ulong)(iVar3 + *puVar5) * 4));
          uVar18 = uVar18 + 1;
          iVar3 = iVar3 + -1;
          *(undefined4 *)(*(long *)(puVar5 + 4) + lVar17) = uVar2;
          uVar4 = (ulong)*puVar5;
        } while (uVar18 < uVar4);
      }
      if (param_4 != (ulong)*puVar5 * 4) {
        uVar9 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_rsa_c_ffff000090800142);
        puVar10 = &DAT_ffff0000907d54f8;
        pcVar11 = s_Signature_length_does_not_match_k_ffff0000907f5ad4;
LAB_ffff0000907be144:
        FUN_ffff0000907c0ee8
                  (uVar9,&DAT_ffff0000907ebfa6,puVar10,s___ERROR__ffff0000907e07dc,pcVar11,0);
        FUN_ffff0000907c1014(puVar5);
        return 0;
      }
      if (param_4 - param_6 != param_8) {
        uVar9 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_rsa_c_ffff000090800142);
        puVar10 = &DAT_ffff0000907fe704;
        pcVar11 = s_Padding_length_does_not_match_ha_ffff0000907d2366;
        goto LAB_ffff0000907be144;
      }
      puVar6 = (uint *)FUN_ffff0000907c1544(param_4);
      if (puVar6 == (uint *)0x0) {
        uVar9 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_rsa_c_ffff000090800142);
        puVar10 = &DAT_ffff0000907dd72c;
        pcVar11 = s_Error_allocating_memory__ffff0000907d54fc;
        goto LAB_ffff0000907be144;
      }
      thunk_FUN_ffff00009077eb20(puVar6,param_3,param_4);
      lVar17 = FUN_ffff0000907c1544((ulong)*puVar5 << 2);
      lVar7 = FUN_ffff0000907c1544((ulong)*puVar5 << 2);
      lVar8 = FUN_ffff0000907c1544((ulong)*puVar5 << 2);
      if (((lVar17 != 0) && (lVar7 != 0)) && (lVar8 != 0)) {
        uVar4 = (ulong)*puVar5;
        if (0 < (int)*puVar5) {
          lVar13 = 0;
          iVar3 = -1;
          do {
            iVar1 = (int)uVar4 + iVar3;
            iVar3 = iVar3 + -1;
            uVar12 = *(uint *)((long)puVar6 + (ulong)(uint)(iVar1 * 4));
            uVar12 = (uVar12 & 0xff00ff00) >> 8 | (uVar12 & 0xff00ff) << 8;
            *(uint *)(lVar17 + lVar13 * 4) = uVar12 >> 0x10 | uVar12 << 0x10;
            lVar13 = lVar13 + 1;
            uVar4 = (ulong)(int)*puVar5;
          } while (lVar13 < (long)uVar4);
        }
        FUN_ffff0000907be1c0(puVar5,lVar7,lVar17,*(undefined8 *)(puVar5 + 4));
        FUN_ffff0000907be1c0(puVar5,lVar8,lVar7,lVar7);
        FUN_ffff0000907be1c0(puVar5,lVar7,lVar8,lVar8);
        FUN_ffff0000907be1c0(puVar5,lVar8,lVar7,lVar7);
        FUN_ffff0000907be1c0(puVar5,lVar7,lVar8,lVar8);
        FUN_ffff0000907be1c0(puVar5,lVar8,lVar7,lVar7);
        FUN_ffff0000907be1c0(puVar5,lVar7,lVar8,lVar8);
        FUN_ffff0000907be1c0(puVar5,lVar8,lVar7,lVar7);
        FUN_ffff0000907be1c0(puVar5,lVar7,lVar8,lVar8);
        FUN_ffff0000907be1c0(puVar5,lVar8,lVar7,lVar7);
        FUN_ffff0000907be1c0(puVar5,lVar7,lVar8,lVar8);
        FUN_ffff0000907be1c0(puVar5,lVar8,lVar7,lVar7);
        FUN_ffff0000907be1c0(puVar5,lVar7,lVar8,lVar8);
        FUN_ffff0000907be1c0(puVar5,lVar8,lVar7,lVar7);
        FUN_ffff0000907be1c0(puVar5,lVar7,lVar8,lVar8);
        FUN_ffff0000907be1c0(puVar5,lVar8,lVar7,lVar7);
        FUN_ffff0000907be1c0(puVar5,lVar7,lVar8,lVar8);
        FUN_ffff0000907be1c0(puVar5,lVar8,lVar7,lVar17);
        uVar12 = *puVar5;
        lVar13 = (ulong)uVar12 * 4;
        do {
          lVar13 = lVar13 + -4;
          if (lVar13 == -4) break;
          if (*(uint *)(lVar8 + lVar13) < *(uint *)(*(long *)(puVar5 + 2) + lVar13))
          goto LAB_ffff0000907bdefc;
        } while (*(uint *)(lVar8 + lVar13) <= *(uint *)(*(long *)(puVar5 + 2) + lVar13));
        if (uVar12 != 0) {
          uVar4 = 0;
          lVar13 = 0;
          lVar14 = *(long *)(puVar5 + 2);
          do {
            lVar16 = uVar4 * 4;
            uVar4 = uVar4 + 1;
            lVar13 = (lVar13 - (ulong)*(uint *)(lVar14 + lVar16)) + (ulong)*(uint *)(lVar8 + lVar16)
            ;
            *(int *)(lVar8 + lVar16) = (int)lVar13;
            lVar13 = lVar13 >> 0x20;
            uVar12 = *puVar5;
          } while (uVar4 < uVar12);
LAB_ffff0000907bdefc:
          if (0 < (int)uVar12) {
            uVar4 = (ulong)uVar12 + 1;
            puVar15 = puVar6;
            do {
              iVar3 = (int)uVar4;
              uVar4 = uVar4 - 1;
              uVar12 = *(uint *)(lVar8 + (ulong)(iVar3 - 2) * 4);
              uVar12 = (uVar12 & 0xff00ff00) >> 8 | (uVar12 & 0xff00ff) << 8;
              *puVar15 = uVar12 >> 0x10 | uVar12 << 0x10;
              puVar15 = puVar15 + 1;
            } while (1 < uVar4);
          }
        }
      }
      if (lVar17 != 0) {
        FUN_ffff0000907c1014(lVar17);
      }
      if (lVar7 != 0) {
        FUN_ffff0000907c1014(lVar7);
      }
      if (lVar8 != 0) {
        FUN_ffff0000907c1014(lVar8);
      }
      iVar3 = FUN_ffff0000907c1080(puVar6,param_7,param_8);
      if (iVar3 == 0) {
        iVar3 = FUN_ffff0000907c1080((long)puVar6 + param_8,param_5,param_6);
        if (iVar3 == 0) {
          uVar2 = 1;
          goto LAB_ffff0000907be1a4;
        }
        uVar9 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_rsa_c_ffff000090800142);
        puVar10 = &DAT_ffff0000907df142;
        pcVar11 = s_Hash_check_failed__ffff0000907e59c5;
      }
      else {
        uVar9 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_rsa_c_ffff000090800142);
        puVar10 = &DAT_ffff0000907e2367;
        pcVar11 = s_Padding_check_failed__ffff0000907fae24;
      }
      FUN_ffff0000907c0ee8
                (uVar9,&DAT_ffff0000907ebfa6,puVar10,s___ERROR__ffff0000907e07dc,pcVar11,0);
      uVar2 = 0;
LAB_ffff0000907be1a4:
      FUN_ffff0000907c1014(puVar5);
      FUN_ffff0000907c1014(puVar6);
      return uVar2;
    }
  }
  uVar9 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_rsa_c_ffff000090800142);
  puVar10 = &DAT_ffff0000907f91a6;
  pcVar11 = s_Error_parsing_key__ffff0000907fe6f0;
LAB_ffff0000907be084:
  FUN_ffff0000907c0ee8(uVar9,&DAT_ffff0000907ebfa6,puVar10,s___ERROR__ffff0000907e07dc,pcVar11,0);
  return 0;
}


