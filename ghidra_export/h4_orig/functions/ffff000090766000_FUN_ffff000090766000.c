// ffff000090766000  size=448  FUN_ffff000090766000

void FUN_ffff000090766000(void)

{
  long *plVar1;
  ulong uVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  
  plVar1 = (long *)FUN_ffff00009077d514(0x400);
  if (plVar1 == (long *)0x0) {
    FUN_ffff00009077e11c
              (s__s__d____s___Alloc_header_buffer_ffff0000907d6ad5,
               s_Judge_write_expdb_to_blackbox_ffff0000907dd14e,0x9e1,s_BLACKBOX_ffff0000907d9a1f);
    return;
  }
  uVar2 = FUN_ffff000090760f14(s_blackbox_ffff0000907f0d8c,plVar1,0xa01000,0x400);
  if ((uVar2 & 1) == 0) {
    FUN_ffff00009077e11c
              (s__s__d____s___get_header_buffer_f_ffff0000907e6b15,
               s_Judge_write_expdb_to_blackbox_ffff0000907dd14e,0x9e6,s_BLACKBOX_ffff0000907d9a1f);
    goto LAB_ffff0000907661ac;
  }
  if (*plVar1 == -0x7337337337337338) {
LAB_ffff0000907660a4:
    if (6 < *(uint *)((long)plVar1 + 0x24)) {
      FUN_ffff00009077e11c
                (s__s__d____s___we_save_expdb_to_bl_ffff0000907fdfd7,
                 s_Judge_write_expdb_to_blackbox_ffff0000907dd14e,0x9fb,s_BLACKBOX_ffff0000907d9a1f)
      ;
      FUN_ffff000090746104(0);
      FUN_ffff000090765e04(plVar1);
      FUN_ffff000090746104(1);
      goto LAB_ffff0000907661ac;
    }
    *(uint *)((long)plVar1 + 0x24) = *(uint *)((long)plVar1 + 0x24) + 1;
    uVar2 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,plVar1,0xa01000,0x400);
    if ((uVar2 & 1) != 0) goto LAB_ffff0000907661ac;
    uVar3 = 0x9f7;
    uVar4 = 0x9f7;
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d____s___init_expdb_table_he_ffff0000907d9b55,
               s_Judge_write_expdb_to_blackbox_ffff0000907dd14e,0x9eb,s_BLACKBOX_ffff0000907d9a1f);
    FUN_ffff000090760da4(plVar1);
    uVar2 = FUN_ffff000090760fd0(s_blackbox_ffff0000907f0d8c,plVar1,0xa01000,0x400);
    if ((uVar2 & 1) != 0) goto LAB_ffff0000907660a4;
    uVar3 = 0x9ef;
    uVar4 = 0x9ef;
  }
  FUN_ffff00009077e11c
            (s__s__d____s____d__write_expdb_hea_ffff0000907f1c5e,
             s_Judge_write_expdb_to_blackbox_ffff0000907dd14e,uVar3,s_BLACKBOX_ffff0000907d9a1f,
             uVar4);
LAB_ffff0000907661ac:
  thunk_FUN_ffff0000907c314c(plVar1);
  return;
}


