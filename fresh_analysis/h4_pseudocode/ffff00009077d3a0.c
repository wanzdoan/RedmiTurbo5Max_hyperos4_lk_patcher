
void FUN_ffff00009077d3a0(void)

{
  DAT_ffff00009087cfb8 = 0;
  DAT_ffff00009087cf78 = &DAT_ffff00009087cf78;
  DAT_ffff00009087cf80 = &DAT_ffff00009087cf78;
  FUN_ffff0000907174cc(&DAT_ffff00009087cf88,0,1);
  DAT_ffff00009087cfc0 =
       FUN_ffff0000907179e4
                 (&DAT_ffff0000907d1e39,FUN_ffff00009077d31c,&DAT_ffff00009087cf78,0x1e,0x2000);
  if (DAT_ffff00009087cfc0 != 0) {
    FUN_ffff000090717bd0();
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s_failed_to_start_default_dpc_queu_ffff0000907ffd32);
}


