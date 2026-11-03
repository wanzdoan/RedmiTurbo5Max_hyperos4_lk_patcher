// ffff000090797bbc  size=80  FUN_ffff000090797bbc

uint FUN_ffff000090797bbc(void)

{
  uint uVar1;
  
  uVar1 = thunk_FUN_ffff00009074af94(6);
  FUN_ffff00009077de54
            (s__s__d___CCCI__Power_On_Dev_en__d_ffff0000907fc641,
             s_ccci_get_md_dev_en_ffff0000907dd486,0x108,uVar1);
  return (uVar1 ^ 0xffffffff) & 1;
}


