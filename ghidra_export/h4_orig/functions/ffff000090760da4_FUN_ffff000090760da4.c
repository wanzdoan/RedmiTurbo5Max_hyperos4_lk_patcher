// ffff000090760da4  size=368  FUN_ffff000090760da4

void FUN_ffff000090760da4(undefined8 *param_1)

{
  long lVar1;
  ulong uVar2;
  char *pcVar3;
  undefined8 uVar4;
  
  if (param_1 == (undefined8 *)0x0) {
    pcVar3 = s__s__d____s___expdb_header_is_nul_ffff000090802cb5;
    uVar4 = 0x1ea;
  }
  else {
    lVar1 = FUN_ffff00009077d514(0x400);
    if (lVar1 == 0) {
      pcVar3 = s__s__d____s___malloc_expdb_header_ffff0000907dcf48;
      uVar4 = 0x1f1;
    }
    else {
      uVar2 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,lVar1,0xa01000,0x400);
      if ((uVar2 & 1) == 0) {
        pcVar3 = s__s__d____s___get_expdb_header_in_ffff0000907de8c5;
        uVar4 = 0x1f6;
      }
      else {
        FUN_ffff00009077ee50(lVar1,0,0x400);
        uVar2 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,lVar1,0xa01000,0x400);
        if ((uVar2 & 1) != 0) {
          thunk_FUN_ffff0000907c314c(lVar1);
          *(undefined4 *)(param_1 + 2) = 0;
          *param_1 = 0x8cc8cc8cc8cc8cc8;
          param_1[1] = 0x40000001000;
          FUN_ffff00009077ee50((long)param_1 + 0x14,0,4);
          FUN_ffff00009077ee50(param_1 + 3,0,4);
          *(undefined4 *)((long)param_1 + 0x24) = 0;
          *(undefined8 *)((long)param_1 + 0x1c) = 0xa0100000f00000;
          return;
        }
        pcVar3 = s__s__d____s___write_expdb_header_i_ffff0000907fa620;
        uVar4 = 0x1fc;
      }
    }
  }
  FUN_ffff00009077e11c
            (pcVar3,s_InitExpdbTableHeader_ffff0000907d4dcd,uVar4,s_BLACKBOX_ffff0000907d9a1f);
  return;
}


