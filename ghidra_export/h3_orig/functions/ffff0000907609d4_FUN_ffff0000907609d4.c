// ffff0000907609d4  size=8  FUN_ffff0000907609d4

void FUN_ffff0000907609d4(undefined8 *param_1)

{
  char cVar1;
  bool bVar2;
  undefined4 uVar3;
  
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d____s___log_header_is_null_ffff0000907f89b1,
               s_InitLogTableHeader_ffff0000907f4fe6,0x19c,s_BLACKBOX_ffff0000907d9988);
    return;
  }
  *(undefined8 *)((long)param_1 + 0xc) = 0x400;
  *param_1 = 0x56775af41bcde0f3;
  cVar1 = DAT_ffff00009087bf14;
  bVar2 = DAT_ffff00009087bf14 == '\x03';
  *(undefined8 *)((long)param_1 + 0x14) = 0x190180000000800;
  *(undefined4 *)((long)param_1 + 0x1c) = 0;
  param_1[4] = 0x980000000a01000;
  param_1[5] = 0x9a0000005c00400;
  if (bVar2) {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v3_ffff0000907d813b,s_InitLogTableHeader_ffff0000907f4fe6,0x1b7);
    uVar3 = 0x3000;
  }
  else {
    if (cVar1 != '\x02') {
      if (cVar1 != '\x01') {
        return;
      }
      FUN_ffff00009077de54
                (s__s__d__blackbox_v1_ffff0000907e50d2,s_InitLogTableHeader_ffff0000907f4fe6,0x1ab);
      param_1[6] = 0;
      *(undefined4 *)(param_1 + 7) = 0;
      *(undefined4 *)(param_1 + 1) = 0x1000;
      return;
    }
    FUN_ffff00009077de54
              (s__s__d__blackbox_v2_ffff0000907d9991,s_InitLogTableHeader_ffff0000907f4fe6,0x1b1);
    uVar3 = 0x2000;
  }
  *(undefined4 *)(param_1 + 1) = uVar3;
  *(undefined4 *)(param_1 + 7) = 0xa440000;
  param_1[6] = 0xa4200000a400000;
  return;
}


