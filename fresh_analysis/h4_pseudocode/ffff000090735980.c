
int FUN_ffff000090735980(void)

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
  
  lVar6 = FUN_ffff00009077bf18(s_bootdevice_ffff0000907d0d09);
  if (lVar6 == 0) {
    return -0x13;
  }
  FUN_ffff000090717738(&LAB_ffff0000908192f0,0xffffffff);
  if (DAT_ffff0000908192e8 == 0) {
    DAT_ffff0000908192d0 = FUN_ffff00009077d55c(0x40,*(undefined8 *)(lVar6 + 0x28));
    if (DAT_ffff0000908192d0 == 0) {
      iVar5 = -0x1c;
      pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000907e7ab0;
    }
    else {
      iVar5 = FUN_ffff000090734828(lVar6,DAT_ffff0000908192d0,*(undefined8 *)(lVar6 + 0x28));
      if (iVar5 != 0) {
        pcVar14 = s__PART_UTILS__gpt_header_get_fail_ffff0000907f14cd;
        FUN_ffff00009077e11c(s__PART_UTILS__gpt_header_get_fail_ffff0000907f14cd);
        iVar5 = FUN_ffff000090734828
                          (lVar6,DAT_ffff0000908192d0,
                           *(long *)(lVar6 + 0x28) * (ulong)(*(int *)(lVar6 + 0x38) - 1));
        if (iVar5 != 0) goto LAB_ffff000090735b80;
      }
      uVar1 = *(uint *)(DAT_ffff0000908192d0 + 0x50);
      DAT_ffff0000908192d8 = FUN_ffff00009077d55c(0x40,(ulong)uVar1 << 7);
      DAT_ffff0000908192e0 = FUN_ffff00009077d55c(0x40,(ulong)uVar1 * 0x6d);
      iVar5 = -0x1c;
      pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000907d7ac1;
      if ((DAT_ffff0000908192d8 != 0) &&
         (pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000907d7ac1, DAT_ffff0000908192e0 != 0)
         ) {
        bVar4 = true;
        bVar3 = true;
        DAT_ffff0000908192e8 = 1;
        if (DAT_ffff0000908192d0 != 0) goto LAB_ffff0000907359f8;
        goto LAB_ffff000090735b60;
      }
    }
LAB_ffff000090735b80:
    FUN_ffff00009077e11c(pcVar14);
    goto LAB_ffff000090735b88;
  }
  bVar3 = DAT_ffff0000908192d8 != 0;
  bVar4 = DAT_ffff0000908192e0 != 0;
  if (DAT_ffff0000908192d0 == 0) {
LAB_ffff000090735b60:
    pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff000090802742;
  }
  else {
LAB_ffff0000907359f8:
    if ((!bVar3) || (!bVar4)) goto LAB_ffff000090735b60;
    uVar1 = *(uint *)(DAT_ffff0000908192d0 + 0x50);
    FUN_ffff00009077ee50(DAT_ffff0000908192d0,0,*(undefined8 *)(lVar6 + 0x28));
    iVar5 = FUN_ffff000090734828(lVar6,DAT_ffff0000908192d0,*(undefined8 *)(lVar6 + 0x28));
    if (iVar5 != 0) {
      FUN_ffff00009077e11c(s__PART_UTILS__gpt_header_get_fail_ffff0000907f14cd);
      iVar5 = FUN_ffff000090734828
                        (lVar6,DAT_ffff0000908192d0,
                         *(long *)(lVar6 + 0x28) * (ulong)(*(int *)(lVar6 + 0x38) - 1));
      if (iVar5 != 0) {
        pcVar14 = s__PART_UTILS__gpt_header_get_fail_ffff0000907f14cd;
        goto LAB_ffff000090735d28;
      }
    }
    lVar8 = (ulong)uVar1 * 0x80;
    uVar9 = (ulong)*(uint *)(DAT_ffff0000908192d0 + 0x50);
    lVar11 = uVar9 * 0x6d;
    lVar13 = uVar9 * 0x80;
    if ((lVar8 + uVar9 * -0x80 != 0) || (uVar1 != *(uint *)(DAT_ffff0000908192d0 + 0x50))) {
      FUN_ffff00009077e11c
                (s__PART_UTILS__update_gpt_netry_tl_ffff0000907e13e0,lVar8,lVar13,
                 (ulong)uVar1 * 0x6d,lVar11);
      thunk_FUN_ffff0000907c314c(DAT_ffff0000908192d8);
      thunk_FUN_ffff0000907c314c(DAT_ffff0000908192e0);
      DAT_ffff0000908192d8 = FUN_ffff00009077d55c(0x40,lVar13);
      DAT_ffff0000908192e0 = FUN_ffff00009077d55c(0x40,lVar11);
      if ((DAT_ffff0000908192d8 == 0) || (DAT_ffff0000908192e0 == 0)) {
        pcVar14 = s__PART_UTILS__lack_of_memory_for_p_ffff0000907d7ac1;
LAB_ffff000090735d28:
        FUN_ffff00009077e11c(pcVar14);
        return -0x1c;
      }
    }
    FUN_ffff00009077ee50(DAT_ffff0000908192d8,0,lVar13);
    FUN_ffff00009077ee50(DAT_ffff0000908192e0,0,lVar11);
    iVar5 = FUN_ffff0000907349cc(lVar6,DAT_ffff0000908192d0,DAT_ffff0000908192d8);
    lVar13 = DAT_ffff0000908192e0;
    lVar11 = DAT_ffff0000908192d8;
    lVar8 = DAT_ffff0000908192d0;
    if (((iVar5 == 0) && (DAT_ffff0000908192d0 != 0)) && (DAT_ffff0000908192d8 != 0)) {
      DAT_ffff0000908192ec = 0;
      if (*(int *)(DAT_ffff0000908192d0 + 0x50) == 0) {
LAB_ffff000090735d34:
        iVar5 = 0;
      }
      else {
        uVar9 = 0;
        lVar15 = DAT_ffff0000908192d8 + 0x38;
        lVar16 = DAT_ffff0000908192e0;
        do {
          pcVar14 = (char *)(lVar11 + uVar9 * 0x80);
          if ((*pcVar14 == '\0') && (pcVar14[1] == '\0')) goto LAB_ffff000090735d34;
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
          lVar7 = FUN_ffff000090734a38(lVar11 + uVar9 * 0x80 + 0x10,lVar13 + uVar9 * 0x6d + 0x48);
          if (lVar7 == 0) {
            FUN_ffff00009077e11c(s__PART_UTILS___s_guid_parse_faile_ffff0000907f14f1,lVar12);
            iVar5 = -0x16;
            break;
          }
          iVar5 = 0;
          uVar9 = uVar9 + 1;
          lVar16 = lVar16 + 0x6d;
          lVar15 = lVar15 + 0x80;
          DAT_ffff0000908192ec = DAT_ffff0000908192ec + 1;
        } while (uVar9 < *(uint *)(lVar8 + 0x50));
      }
      goto LAB_ffff000090735b88;
    }
    pcVar14 = s__PART_UTILS__get_GPT_failed_ffff0000907d45fc;
  }
  FUN_ffff00009077e11c(pcVar14);
  iVar5 = -0x1c;
LAB_ffff000090735b88:
  FUN_ffff0000907177d0(&LAB_ffff0000908192f0);
  FUN_ffff00009077bfac(lVar6);
  return iVar5;
}


