// ffff000090746dfc  size=216  FUN_ffff000090746dfc

undefined8 FUN_ffff000090746dfc(long param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  undefined4 local_4;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_clkbuf__ffff0000907d9517,100,
               s_fdt____NULL_ffff0000907ff42d);
  }
  local_4 = 0;
  iVar1 = FUN_ffff00009076e32c(param_1,0,PTR_s_mediatek_mt6991_consys_ffff00009081a6e8);
  if (iVar1 < 0) {
    uVar3 = 2;
  }
  else {
    lVar2 = FUN_ffff00009076df9c(param_1,iVar1,DAT_ffff00009081a6f0,&local_4);
    if ((lVar2 != 0) && (iVar1 = FUN_ffff00009077f0ac(lVar2,&DAT_ffff0000907de415), iVar1 == 0)) {
      FUN_ffff00009077e11c
                (s__CLKBUF___s_support_ffff0000907d7cf0,PTR_s_TCXO_SUPPORT_ffff00009081a6d8);
      (*(code *)PTR_FUN_ffff00009081a6e0)();
      goto LAB_ffff000090746e78;
    }
    uVar3 = 4;
  }
  FUN_ffff00009077e11c
            (s__CLKBUF___s_not_support__ret___d_ffff0000907fd821,PTR_s_TCXO_SUPPORT_ffff00009081a6d8
             ,uVar3);
LAB_ffff000090746e78:
  FUN_ffff00009077e11c(s__CLKBUF_LK__Init_Done_ffff000090800e9c);
  return 0;
}


