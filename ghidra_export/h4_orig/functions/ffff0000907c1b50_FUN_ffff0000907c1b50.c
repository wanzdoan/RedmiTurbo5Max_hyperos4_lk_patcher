// ffff0000907c1b50  size=784  FUN_ffff0000907c1b50

undefined1 * FUN_ffff0000907c1b50(ulong param_1)

{
  uint uVar1;
  uint uVar2;
  bool bVar3;
  ulong uVar4;
  undefined1 *puVar5;
  undefined8 uVar6;
  undefined *puVar7;
  char *pcVar8;
  ulong *puVar9;
  ulong uVar10;
  undefined1 *puVar11;
  long lVar12;
  long lVar13;
  ulong local_270 [64];
  ulong *puVar14;
  ulong *puVar15;
  
  uVar10 = 0;
  lVar12 = 0;
  uVar2 = 0xffffffc8;
  local_270[0x3f] = 0;
  local_270[0x3e] = 0;
  local_270[0x3d] = 0;
  local_270[0x3c] = 0;
  local_270[0x3b] = 0;
  local_270[0x3a] = 0;
  local_270[0x39] = 0;
  local_270[0x37] = 0;
  local_270[0x38] = 0;
  local_270[0x35] = 0;
  local_270[0x36] = 0;
  local_270[0x33] = 0;
  local_270[0x34] = 0;
  local_270[0x31] = 0;
  local_270[0x32] = 0;
  local_270[0x2f] = 0;
  local_270[0x30] = 0;
  local_270[0x2d] = 0;
  local_270[0x2e] = 0;
  local_270[0x2b] = 0;
  local_270[0x2c] = 0;
  local_270[0x29] = 0;
  local_270[0x2a] = 0;
  local_270[0x27] = 0;
  local_270[0x28] = 0;
  local_270[0x25] = 0;
  local_270[0x26] = 0;
  local_270[0x23] = 0;
  local_270[0x24] = 0;
  local_270[0x21] = 0;
  local_270[0x22] = 0;
  local_270[0x1f] = 0;
  local_270[0x20] = 0;
  local_270[0x1d] = 0;
  local_270[0x1e] = 0;
  local_270[0x1b] = 0;
  local_270[0x1c] = 0;
  local_270[0x19] = 0;
  local_270[0x1a] = 0;
  local_270[0x17] = 0;
  local_270[0x18] = 0;
  local_270[0x15] = 0;
  local_270[0x16] = 0;
  local_270[0x13] = 0;
  local_270[0x14] = 0;
  local_270[0x11] = 0;
  local_270[0x12] = 0;
  local_270[0xf] = 0;
  local_270[0x10] = 0;
  local_270[0xd] = 0;
  local_270[0xe] = 0;
  local_270[0xb] = 0;
  local_270[0xc] = 0;
  local_270[9] = 0;
  local_270[10] = 0;
  local_270[7] = 0;
  local_270[8] = 0;
  local_270[5] = 0;
  local_270[6] = 0;
  local_270[3] = 0;
  local_270[4] = 0;
  local_270[1] = 0;
  local_270[2] = 0;
  local_270[0] = 0;
  puVar15 = (ulong *)register0x00000008;
  do {
    uVar4 = thunk_FUN_ffff00009077f250(param_1);
    bVar3 = CARRY8(uVar4,uVar10);
    uVar10 = uVar4 + uVar10;
    local_270[lVar12 + 0x20] = param_1;
    local_270[lVar12] = uVar4;
    if (bVar3) {
      pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5585;
      lVar13 = thunk_FUN_ffff00009077f250(s_external_lib_libavb_avb_util_c_ffff0000907d5585);
      lVar12 = lVar13 + -2;
      if (lVar13 + -2 < 0) goto LAB_ffff0000907c1dfc;
      goto LAB_ffff0000907c1da4;
    }
    if (lVar12 == 0x1f) {
      uVar6 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_util_c_ffff0000907d5585);
      puVar7 = &DAT_ffff0000907f3d70;
      pcVar8 = s_Too_many_strings_passed__ffff0000907d8a8c;
      goto LAB_ffff0000907c1e54;
    }
    uVar1 = uVar2;
    if (((int)uVar2 < 0) && (uVar1 = uVar2 + 8, uVar2 < 0xfffffff9)) {
      puVar9 = (ulong *)((long)local_270 + (long)(int)uVar2);
      puVar14 = puVar15;
      uVar2 = uVar1;
    }
    else {
      puVar14 = puVar15 + 1;
      puVar9 = puVar15;
      uVar2 = uVar1;
    }
    param_1 = *puVar9;
    lVar12 = lVar12 + 1;
    puVar15 = puVar14;
  } while (param_1 != 0);
  puVar5 = (undefined1 *)FUN_ffff0000907c0f94(uVar10 + 1);
  if (puVar5 == (undefined1 *)0x0) {
    pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5585;
    lVar13 = thunk_FUN_ffff00009077f250(s_external_lib_libavb_avb_util_c_ffff0000907d5585);
    lVar12 = lVar13 + -2;
    if (-1 < lVar13 + -2) {
      do {
        if (s_external_lib_libavb_avb_util_c_ffff0000907d5585[lVar12] == '/') {
          pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5585 + lVar12 + 1;
          break;
        }
        bVar3 = 0 < lVar12;
        lVar12 = lVar12 + -1;
      } while (bVar3);
    }
    FUN_ffff0000907c0ee8
              (pcVar8,&DAT_ffff0000907ebfa6,&DAT_ffff0000907e23db,s___ERROR__ffff0000907e07dc,
               s_Failed_to_allocate_memory__ffff00009080366c,0);
    puVar5 = (undefined1 *)0x0;
  }
  else {
    lVar13 = 0;
    puVar11 = puVar5;
    do {
      uVar10 = local_270[lVar13];
      thunk_FUN_ffff00009077eb20(puVar11,local_270[lVar13 + 0x20],uVar10);
      puVar11 = puVar11 + uVar10;
      lVar13 = lVar13 + 1;
    } while (lVar12 != lVar13);
    *puVar11 = 0;
  }
  return puVar5;
  while (bVar3 = 0 < lVar12, lVar12 = lVar12 + -1, bVar3) {
LAB_ffff0000907c1da4:
    if (s_external_lib_libavb_avb_util_c_ffff0000907d5585[lVar12] == '/') {
      pcVar8 = s_external_lib_libavb_avb_util_c_ffff0000907d5585 + lVar12 + 1;
      break;
    }
  }
LAB_ffff0000907c1dfc:
  FUN_ffff0000907c0ee8
            (pcVar8,&DAT_ffff0000907ebfa6,&DAT_ffff000090801b7b,s___ERROR__ffff0000907e07dc,
             s_Overflow_when_adding_values__ffff00009080364e,0);
  uVar6 = FUN_ffff0000907c11dc(s_external_lib_libavb_avb_util_c_ffff0000907d5585);
  puVar7 = &DAT_ffff0000907ebd02;
  pcVar8 = s_Overflow_while_determining_total_ffff0000907d55a4;
LAB_ffff0000907c1e54:
  FUN_ffff0000907c0ee8(uVar6,&DAT_ffff0000907ebfa6,puVar7,s___FATAL__ffff0000907f5b19,pcVar8,0);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000907c0ee4();
}


