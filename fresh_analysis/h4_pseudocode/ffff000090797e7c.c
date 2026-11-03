
uint FUN_ffff000090797e7c(void)

{
  uint uVar1;
  
  uVar1 = thunk_FUN_ffff00009074b22c(6);
  FUN_ffff00009077e11c
            (s__s__d___CCCI__Power_On_Dev_en__d_ffff0000907fc7dd,
             s_ccci_get_md_dev_en_ffff0000907dd540,0x108,uVar1);
  return (uVar1 ^ 0xffffffff) & 1;
}


