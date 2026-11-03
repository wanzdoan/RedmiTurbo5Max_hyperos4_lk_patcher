
int FUN_ffff0000b801fb04(void)

{
  uint uVar1;
  short sVar2;
  bool bVar3;
  bool bVar4;
  int iVar5;
  long lVar6;
  long lVar7;
  long lVar8;
  ulong uVar9;
  ulong uVar10;
  long lVar11;
  long lVar12;
  long lVar13;
  char *pcVar14;
  long lVar15;
  long lVar16;
  
  lVar6 = FUN_ffff0000b804a88c(s_bootdevice_ffff0000b80863d5);
  if (lVar6 == 0) {
    return -0x13;
  }
  FUN_ffff0000b800ad90(&LAB_ffff0000b80e90c8,0xffffffff);
  if (DAT_ffff0000b80e90c0 == 0) {
    DAT_ffff0000b80e90a8 = FUN_ffff0000b804bd14(0x40,*(undefined8 *)(lVar6 + 0x28));
    if (DAT_ffff0000b80e90a8 == 0) {
      iVar5 = -0x1c;
      pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000b8092620;
    }
    else {
      iVar5 = FUN_ffff0000b801f4d8(lVar6,DAT_ffff0000b80e90a8,*(undefined8 *)(lVar6 + 0x28));
      if (iVar5 != 0) {
        pcVar14 = s__PART_UTILS__gpt_header_get_fail_ffff0000b8098002;
        FUN_ffff0000b804c33c(s__PART_UTILS__gpt_header_get_fail_ffff0000b8098002);
        iVar5 = FUN_ffff0000b801f4d8
                          (lVar6,DAT_ffff0000b80e90a8,
                           *(long *)(lVar6 + 0x28) * (ulong)(*(int *)(lVar6 + 0x38) - 1));
        if (iVar5 != 0) goto LAB_ffff0000b801fd04;
      }
      uVar1 = *(uint *)(DAT_ffff0000b80e90a8 + 0x50);
      DAT_ffff0000b80e90b0 = FUN_ffff0000b804bd14(0x40,(ulong)uVar1 << 7);
      DAT_ffff0000b80e90b8 = FUN_ffff0000b804bd14(0x40,(ulong)uVar1 * 0x6d);
      iVar5 = -0x1c;
      pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000b8089bbd;
      if ((DAT_ffff0000b80e90b0 != 0) &&
         (pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000b8089bbd, DAT_ffff0000b80e90b8 != 0)
         ) {
        bVar4 = true;
        bVar3 = true;
        DAT_ffff0000b80e90c0 = 1;
        if (DAT_ffff0000b80e90a8 != 0) goto LAB_ffff0000b801fb7c;
        goto LAB_ffff0000b801fce4;
      }
    }
LAB_ffff0000b801fd04:
    FUN_ffff0000b804c33c(pcVar14);
    goto LAB_ffff0000b801fd0c;
  }
  bVar3 = DAT_ffff0000b80e90b0 != 0;
  bVar4 = DAT_ffff0000b80e90b8 != 0;
  if (DAT_ffff0000b80e90a8 == 0) {
LAB_ffff0000b801fce4:
    pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000b80a1064;
  }
  else {
LAB_ffff0000b801fb7c:
    if ((!bVar3) || (!bVar4)) goto LAB_ffff0000b801fce4;
    uVar1 = *(uint *)(DAT_ffff0000b80e90a8 + 0x50);
    FUN_ffff0000b804d070(DAT_ffff0000b80e90a8,0,*(undefined8 *)(lVar6 + 0x28));
    iVar5 = FUN_ffff0000b801f4d8(lVar6,DAT_ffff0000b80e90a8,*(undefined8 *)(lVar6 + 0x28));
    if (iVar5 != 0) {
      FUN_ffff0000b804c33c(s__PART_UTILS__gpt_header_get_fail_ffff0000b8098002);
      iVar5 = FUN_ffff0000b801f4d8
                        (lVar6,DAT_ffff0000b80e90a8,
                         *(long *)(lVar6 + 0x28) * (ulong)(*(int *)(lVar6 + 0x38) - 1));
      if (iVar5 != 0) {
        pcVar14 = s__PART_UTILS__gpt_header_get_fail_ffff0000b8098002;
        goto LAB_ffff0000b801feac;
      }
    }
    lVar8 = (ulong)uVar1 * 0x80;
    uVar9 = (ulong)*(uint *)(DAT_ffff0000b80e90a8 + 0x50);
    lVar11 = uVar9 * 0x6d;
    lVar13 = uVar9 * 0x80;
    if ((lVar8 + uVar9 * -0x80 != 0) || (uVar1 != *(uint *)(DAT_ffff0000b80e90a8 + 0x50))) {
      FUN_ffff0000b804c33c
                (s__PART_UTILS__update_gpt_netry_tl_ffff0000b808ecea,lVar8,lVar13,
                 (ulong)uVar1 * 0x6d,lVar11);
      thunk_FUN_ffff0000b8082558(DAT_ffff0000b80e90b0);
      thunk_FUN_ffff0000b8082558(DAT_ffff0000b80e90b8);
      DAT_ffff0000b80e90b0 = FUN_ffff0000b804bd14(0x40,lVar13);
      DAT_ffff0000b80e90b8 = FUN_ffff0000b804bd14(0x40,lVar11);
      if ((DAT_ffff0000b80e90b0 == 0) || (DAT_ffff0000b80e90b8 == 0)) {
        pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000b8089bbd;
LAB_ffff0000b801feac:
        FUN_ffff0000b804c33c(pcVar14);
        return -0x1c;
      }
    }
    FUN_ffff0000b804d070(DAT_ffff0000b80e90b0,0,lVar13);
    FUN_ffff0000b804d070(DAT_ffff0000b80e90b8,0,lVar11);
    iVar5 = FUN_ffff0000b801f67c(lVar6,DAT_ffff0000b80e90a8,DAT_ffff0000b80e90b0);
    lVar13 = DAT_ffff0000b80e90b8;
    lVar11 = DAT_ffff0000b80e90b0;
    lVar8 = DAT_ffff0000b80e90a8;
    if (((iVar5 == 0) && (DAT_ffff0000b80e90a8 != 0)) && (DAT_ffff0000b80e90b0 != 0)) {
      DAT_ffff0000b80e90c4 = 0;
      if (*(int *)(DAT_ffff0000b80e90a8 + 0x50) == 0) {
LAB_ffff0000b801feb8:
        iVar5 = 0;
      }
      else {
        uVar9 = 0;
        lVar15 = DAT_ffff0000b80e90b0 + 0x38;
        lVar16 = DAT_ffff0000b80e90b8;
        do {
          pcVar14 = (char *)(lVar11 + uVar9 * 0x80);
          if ((*pcVar14 == '\0') && (pcVar14[1] == '\0')) goto LAB_ffff0000b801feb8;
          lVar12 = lVar13 + uVar9 * 0x6d;
          uVar10 = 0;
          do {
            sVar2 = *(short *)(lVar15 + uVar10 * 2);
            if (sVar2 == 0) break;
            *(char *)(lVar16 + uVar10) = (char)sVar2;
            uVar10 = uVar10 + 1;
          } while (uVar10 != 0x47);
          *(undefined1 *)(lVar12 + (uVar10 & 0xffffffff)) = 0;
          *(undefined1 *)(lVar12 + 0x47) = 0;
          lVar7 = FUN_ffff0000b801f6e8(lVar11 + uVar9 * 0x80 + 0x10,lVar13 + uVar9 * 0x6d + 0x48);
          if (lVar7 == 0) {
            FUN_ffff0000b804c33c(s__PART_UTILS___s_guid_parse_faile_ffff0000b8098026,lVar12);
            iVar5 = -0x16;
            break;
          }
          iVar5 = 0;
          uVar9 = uVar9 + 1;
          lVar16 = lVar16 + 0x6d;
          lVar15 = lVar15 + 0x80;
          DAT_ffff0000b80e90c4 = DAT_ffff0000b80e90c4 + 1;
        } while (uVar9 < *(uint *)(lVar8 + 0x50));
      }
      goto LAB_ffff0000b801fd0c;
    }
    pcVar14 = s__PART_UTILS__get_GPT_failed_ffff0000b8088168;
  }
  FUN_ffff0000b804c33c(pcVar14);
  iVar5 = -0x1c;
LAB_ffff0000b801fd0c:
  FUN_ffff0000b800ae28(&LAB_ffff0000b80e90c8);
  FUN_ffff0000b804a920(lVar6);
  return iVar5;
}


