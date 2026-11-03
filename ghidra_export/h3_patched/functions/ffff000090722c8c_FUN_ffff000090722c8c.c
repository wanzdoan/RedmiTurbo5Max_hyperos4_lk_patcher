// ffff000090722c8c  size=1400  FUN_ffff000090722c8c

void FUN_ffff000090722c8c(code *param_1)

{
  long lVar1;
  undefined8 uVar2;
  ulong uVar3;
  long lVar4;
  undefined4 *puVar5;
  char *pcVar6;
  undefined4 local_84;
  undefined4 uStack_80;
  undefined4 local_7c;
  undefined4 uStack_78;
  undefined4 local_74;
  undefined4 local_70;
  undefined4 uStack_6c;
  undefined4 local_68;
  undefined4 local_64;
  
  lVar1 = FUN_ffff00009077d24c(0x24);
  if (lVar1 == 0) {
    FUN_ffff00009077de54(s_Fail_to_alloc_memory__ffff0000907f964b);
    return;
  }
  local_68 = 0x6991;
  local_74 = 0x4000;
  local_64 = 2;
  local_7c = 0x1c;
  uStack_78 = 0x6c;
  local_70 = 4;
  uStack_6c = 4;
  local_84 = 0x80;
  uStack_80 = 0x84;
  FUN_ffff00009077e858(lVar1,&local_64,4);
  FUN_ffff00009077e858(lVar1 + 4,&local_68,4);
  FUN_ffff00009077e858(lVar1 + 8,&uStack_6c,4);
  FUN_ffff00009077e858(lVar1 + 0xc,&local_70,4);
  FUN_ffff00009077e858(lVar1 + 0x10,&local_74,4);
  FUN_ffff00009077e858(lVar1 + 0x14,&uStack_78,4);
  FUN_ffff00009077e858(lVar1 + 0x18,&local_7c,4);
  FUN_ffff00009077e858(lVar1 + 0x1c,&uStack_80,4);
  FUN_ffff00009077e858(lVar1 + 0x20,&local_84,4);
  uVar2 = FUN_ffff00009077b06c(lVar1);
  uVar3 = (*param_1)(uVar2,0x24);
  if ((uVar3 & 1) == 0) {
    FUN_ffff00009077de54(s_DPM_Header_dump_failed__ffff0000907f5f7c);
  }
  thunk_FUN_ffff0000907c2ad8(lVar1);
  lVar1 = 0;
  do {
    lVar4 = FUN_ffff00009077d24c(0x4000);
    if (lVar4 == 0) goto LAB_ffff0000907231a0;
    FUN_ffff00009077e858(lVar4,&DAT_ffff000010920000 + lVar1,0x4000);
    uVar2 = FUN_ffff00009077b06c(lVar4);
    uVar3 = (*param_1)(uVar2,0x4000);
    if ((uVar3 & 1) == 0) {
      FUN_ffff00009077de54(s_DPM_DM1_SRAM_BASE_dump_failed__ffff0000907e4441);
    }
    thunk_FUN_ffff0000907c2ad8(lVar4);
    lVar4 = FUN_ffff00009077d24c(0x6c);
    if (lVar4 == 0) goto LAB_ffff0000907231a0;
    FUN_ffff00009077e858(lVar4,lVar1 + -0xffffef6b8c80,0x6c);
    uVar2 = FUN_ffff00009077b06c(lVar4);
    uVar3 = (*param_1)(uVar2,0x6c);
    if ((uVar3 & 1) == 0) {
      FUN_ffff00009077de54(s_DPM_DBG_LATCH_CH0_dump_failed__ffff0000907f5f95);
    }
    thunk_FUN_ffff0000907c2ad8(lVar4);
    puVar5 = (undefined4 *)FUN_ffff00009077d24c(0x1c);
    if (puVar5 == (undefined4 *)0x0) goto LAB_ffff0000907231a0;
    FUN_ffff00009077eb88(puVar5,0,0x1c);
    *puVar5 = *(undefined4 *)(lVar1 + -0xffffef6bff9c);
    puVar5[1] = *(undefined4 *)(lVar1 + -0xffffef6bff98);
    puVar5[2] = *(undefined4 *)(lVar1 + -0xffffef6bff94);
    puVar5[3] = *(undefined4 *)(lVar1 + -0xffffef6bff90);
    puVar5[4] = *(undefined4 *)(lVar1 + -0xffffef6bff8c);
    puVar5[5] = *(undefined4 *)(lVar1 + -0xffffef6bfeb4);
    puVar5[6] = *(undefined4 *)(lVar1 + -0xffffef6bfeb0);
    uVar2 = FUN_ffff00009077b06c(puVar5);
    uVar3 = (*param_1)(uVar2,0x1c);
    if ((uVar3 & 1) == 0) {
      FUN_ffff00009077de54(s_DPM_CFG1_CH0_dump_failed__ffff0000907fb357);
    }
    thunk_FUN_ffff0000907c2ad8(puVar5);
    puVar5 = (undefined4 *)FUN_ffff00009077d24c(0x84);
    if (puVar5 == (undefined4 *)0x0) {
      FUN_ffff00009077de54(s_malloc_for_pc_latch_failed_ffff0000907edd24);
    }
    else {
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 0;
      *puVar5 = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[1] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 1;
      puVar5[2] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[3] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 2;
      puVar5[4] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[5] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 3;
      puVar5[6] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[7] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 4;
      puVar5[8] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[9] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 5;
      puVar5[10] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0xb] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 6;
      puVar5[0xc] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0xd] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 7;
      puVar5[0xe] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0xf] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 8;
      puVar5[0x10] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0x11] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 9;
      puVar5[0x12] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0x13] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 10;
      puVar5[0x14] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0x15] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 0xb;
      puVar5[0x16] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0x17] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 0xc;
      puVar5[0x18] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0x19] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 0xd;
      puVar5[0x1a] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0x1b] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 0xe;
      puVar5[0x1c] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0x1d] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      *(undefined4 *)(lVar1 + -0xffffef6bfe88) = 0xf;
      puVar5[0x1e] = *(undefined4 *)(lVar1 + -0xffffef6bfe84);
      puVar5[0x1f] = *(undefined4 *)(lVar1 + -0xffffef6bfe80);
      puVar5[0x20] = *(undefined4 *)(lVar1 + -0xffffef6bffb4);
      uVar2 = FUN_ffff00009077b06c();
      uVar3 = (*param_1)(uVar2,0x84);
      if ((uVar3 & 1) == 0) {
        FUN_ffff00009077de54(s_DPM_last_16_pc_dump_failed__ffff0000907f5fb5);
      }
      thunk_FUN_ffff0000907c2ad8(puVar5);
    }
    lVar1 = lVar1 + 0x100000;
  } while (lVar1 != 0x400000);
  lVar1 = FUN_ffff00009077d24c(0x20);
  if (lVar1 == 0) {
LAB_ffff0000907231a0:
    pcVar6 = s_Fail_to_alloc_memory__ffff0000907f964b;
    goto LAB_ffff0000907231a8;
  }
  FUN_ffff00009077e858(lVar1,0xffff000010231600,0x20);
  uVar2 = FUN_ffff00009077b06c(lVar1);
  uVar3 = (*param_1)(uVar2,0x20);
  if ((uVar3 & 1) == 0) {
    uVar2 = 0;
LAB_ffff0000907231e4:
    FUN_ffff00009077de54(s_channel_index____d_dump_failed__ffff00009080062e,uVar2);
  }
  else {
    FUN_ffff00009077e858(lVar1,0xffff000010241600,0x20);
    uVar2 = FUN_ffff00009077b06c(lVar1);
    uVar3 = (*param_1)(uVar2,0x20);
    if ((uVar3 & 1) == 0) {
      uVar2 = 1;
      goto LAB_ffff0000907231e4;
    }
    FUN_ffff00009077e858(lVar1,0xffff000010251600,0x20);
    uVar2 = FUN_ffff00009077b06c(lVar1);
    uVar3 = (*param_1)(uVar2,0x20);
    if ((uVar3 & 1) == 0) {
      uVar2 = 2;
      goto LAB_ffff0000907231e4;
    }
    FUN_ffff00009077e858(lVar1,0xffff000010261600,0x20);
    uVar2 = FUN_ffff00009077b06c(lVar1);
    uVar3 = (*param_1)(uVar2,0x20);
    if ((uVar3 & 1) == 0) {
      uVar2 = 3;
      goto LAB_ffff0000907231e4;
    }
  }
  thunk_FUN_ffff0000907c2ad8(lVar1);
  pcVar6 = s_END_DPM_DUMP__ffff0000907cf405;
LAB_ffff0000907231a8:
  FUN_ffff00009077de54(pcVar6);
  return;
}


