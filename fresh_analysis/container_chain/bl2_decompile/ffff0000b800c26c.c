
void FUN_ffff0000b800c26c(undefined8 param_1,uint param_2)

{
  int iVar1;
  int *piVar2;
  uint uVar3;
  
  piVar2 = (int *)FUN_ffff0000b8049e60();
  DAT_ffff0000b81104e8 = piVar2;
  if ((param_2 < 0x100) || (piVar2 == (int *)0x0)) {
    DAT_ffff0000b81104f0 = 1;
    FUN_ffff0000b804c33c
              (s__s__log_store_init_failed__ffff0000b8086f1d,s_LOG_STORE_LK_ffff0000b8089911);
    DAT_ffff0000b81104f0 = 0;
    return;
  }
  if (*piVar2 != 0x24013186) {
    DAT_ffff0000b81104f0 = 1;
    FUN_ffff0000b804c33c
              (s__s_sram_header_0x_p_is_not_match_ffff0000b8092335,s_LOG_STORE_LK_ffff0000b8089911,
               piVar2);
    DAT_ffff0000b81104f0 = 0;
    FUN_ffff0000b804d070(DAT_ffff0000b81104e8,0,0x100);
    piVar2 = DAT_ffff0000b81104e8;
    *DAT_ffff0000b81104e8 = 0x24013186;
  }
  iVar1 = piVar2[3];
  if (iVar1 == 0x5678ef90) {
    if (piVar2[0x10] == -0x3254cbee) goto LAB_ffff0000b800c3ec;
    DAT_ffff0000b81104f0 = 1;
    FUN_ffff0000b804c33c
              (s__s__BUFF_ERROR__sig_0x_x__buff_s_ffff0000b809990c,s_LOG_STORE_LK_ffff0000b8089911,
               piVar2[0x10],piVar2[7],piVar2[0x12]);
    DAT_ffff0000b81104f0 = 0;
    piVar2 = DAT_ffff0000b81104e8;
  }
  else {
    DAT_ffff0000b81104f0 = 1;
    FUN_ffff0000b804c33c
              (s__s_sram_dram_buff_0x_p__sig_0x_x_ffff0000b8095197,s_LOG_STORE_LK_ffff0000b8089911,
               piVar2 + 3,iVar1,piVar2[4]);
    DAT_ffff0000b81104f0 = 0;
    FUN_ffff0000b804d070(DAT_ffff0000b81104e8 + 3,0,0x34);
    piVar2 = DAT_ffff0000b81104e8;
    DAT_ffff0000b81104e8[3] = 0x5678ef90;
    piVar2[4] = 0x80;
  }
  FUN_ffff0000b804d070(piVar2 + 0x10,0,0x20);
  piVar2 = DAT_ffff0000b81104e8;
  DAT_ffff0000b81104e8[0x10] = -0x3254cbee;
LAB_ffff0000b800c3ec:
  uVar3 = piVar2[0x12] + piVar2[0x13];
  if ((uint)piVar2[7] <= uVar3) {
    DAT_ffff0000b81104f0 = 1;
    FUN_ffff0000b804c33c
              (s__s__buff_full_pl_size_0x_x__ffff0000b8099944,s_LOG_STORE_LK_ffff0000b8089911);
    piVar2 = DAT_ffff0000b81104e8;
    DAT_ffff0000b81104f0 = 0;
    uVar3 = DAT_ffff0000b81104e8[0x13] + DAT_ffff0000b81104e8[0x12];
    DAT_ffff0000b81104e8[4] = DAT_ffff0000b81104e8[4] | 0x10;
  }
  piVar2[0x15] = uVar3;
  piVar2[0x17] = 0xc;
  FUN_ffff0000b8082710(&PTR_LOOP_ffff0000b80adcd0);
  return;
}


