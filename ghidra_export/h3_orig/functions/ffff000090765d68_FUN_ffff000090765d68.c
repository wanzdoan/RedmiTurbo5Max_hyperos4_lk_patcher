// ffff000090765d68  size=448  FUN_ffff000090765d68

void FUN_ffff000090765d68(void)

{
  long *plVar1;
  ulong uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  
  plVar1 = (long *)FUN_ffff00009077d24c(0x400);
  if (plVar1 == (long *)0x0) {
    FUN_ffff00009077de54
              (s__s__d____s___Alloc_header_buffer_ffff0000907d6a3f,
               s_Judge_write_expdb_to_blackbox_ffff0000907dd078,0x9e1,s_BLACKBOX_ffff0000907d9988);
    return;
  }
  uVar2 = FUN_ffff000090760c7c(s_blackbox_ffff0000907f0c1b,plVar1,0xa01000,0x400);
  if ((uVar2 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__d____s___get_header_buffer_f_ffff0000907e6a01,
               s_Judge_write_expdb_to_blackbox_ffff0000907dd078,0x9e6,s_BLACKBOX_ffff0000907d9988);
    goto LAB_ffff000090765f14;
  }
  if (*plVar1 == -0x7337337337337338) {
LAB_ffff000090765e0c:
    if (6 < *(uint *)((long)plVar1 + 0x24)) {
      FUN_ffff00009077de54
                (s__s__d____s___we_save_expdb_to_bl_ffff0000907fde2f,
                 s_Judge_write_expdb_to_blackbox_ffff0000907dd078,0x9fb,s_BLACKBOX_ffff0000907d9988)
      ;
      FUN_ffff000090745e6c(0);
      FUN_ffff000090765b6c(plVar1);
      FUN_ffff000090745e6c(1);
      goto LAB_ffff000090765f14;
    }
    *(uint *)((long)plVar1 + 0x24) = *(uint *)((long)plVar1 + 0x24) + 1;
    uVar2 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,plVar1,0xa01000,0x400);
    if ((uVar2 & 1) != 0) goto LAB_ffff000090765f14;
    uVar3 = 0x9f7;
    uVar4 = 0x9f7;
  }
  else {
    FUN_ffff00009077de54
              (s__s__d____s___init_expdb_table_he_ffff0000907d9abe,
               s_Judge_write_expdb_to_blackbox_ffff0000907dd078,0x9eb,s_BLACKBOX_ffff0000907d9988);
    FUN_ffff000090760b0c(plVar1);
    uVar2 = FUN_ffff000090760d38(s_blackbox_ffff0000907f0c1b,plVar1,0xa01000,0x400);
    if ((uVar2 & 1) != 0) goto LAB_ffff000090765e0c;
    uVar3 = 0x9ef;
    uVar4 = 0x9ef;
  }
  FUN_ffff00009077de54
            (s__s__d____s____d__write_expdb_hea_ffff0000907f1aed,
             s_Judge_write_expdb_to_blackbox_ffff0000907dd078,uVar3,s_BLACKBOX_ffff0000907d9988,
             uVar4);
LAB_ffff000090765f14:
  thunk_FUN_ffff0000907c2ad8(plVar1);
  return;
}


