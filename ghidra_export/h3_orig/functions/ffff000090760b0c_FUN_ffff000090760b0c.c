// ffff000090760b0c  size=368  FUN_ffff000090760b0c

void FUN_ffff000090760b0c(undefined8 *param_1)

{
  long lVar1;
  ulong uVar2;
  char *pcVar3;
  undefined8 uVar4;
  
  if (param_1 == (undefined8 *)0x0) {
    pcVar3 = s__s__d____s___expdb_header_is_nul_ffff000090802ab1;
    uVar4 = 0x1ea;
  }
  else {
    lVar1 = FUN_ffff00009077d24c(0x400);
    if (lVar1 == 0) {
      pcVar3 = s__s__d____s___malloc_expdb_header_ffff0000907dce72;
      uVar4 = 0x1f1;
    }
    else {
      uVar2 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c1b,lVar1,0xa01000,0x400);
      if ((uVar2 & 1) == 0) {
        pcVar3 = s__s__d____s___get_expdb_header_in_ffff0000907de822;
        uVar4 = 0x1f6;
      }
      else {
        FUN_ffff00009077eb88(lVar1,0,0x400);
        uVar2 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,lVar1,0xa01000,0x400);
        if ((uVar2 & 1) != 0) {
          thunk_FUN_ffff0000907c2ad8(lVar1);
          *(undefined4 *)(param_1 + 2) = 0;
          *param_1 = 0x8cc8cc8cc8cc8cc8;
          param_1[1] = 0x40000001000;
          FUN_ffff00009077eb88((long)param_1 + 0x14,0,4);
          FUN_ffff00009077eb88(param_1 + 3,0,4);
          *(undefined4 *)((long)param_1 + 0x24) = 0;
          *(undefined8 *)((long)param_1 + 0x1c) = 0xa0100000f00000;
          return;
        }
        pcVar3 = s__s__d____s___write_expdb_header_i_ffff0000907fa484;
        uVar4 = 0x1fc;
      }
    }
  }
  FUN_ffff00009077de54
            (pcVar3,s_InitExpdbTableHeader_ffff0000907d4d90,uVar4,s_BLACKBOX_ffff0000907d9988);
  return;
}


