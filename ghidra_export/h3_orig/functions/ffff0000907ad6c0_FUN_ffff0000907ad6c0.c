// ffff0000907ad6c0  size=220  FUN_ffff0000907ad6c0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907ad6c0(void)

{
  short sVar1;
  int iVar2;
  
  sVar1 = _DAT_ffff00009090c022;
  iVar2 = FUN_ffff00009077e820(0xffff00009090c010,s_AND_ROMINFO_v_ffff0000907e9fc3,0xd);
  if (iVar2 != 0) {
    FUN_ffff00009077de54
              (s___s__Rom_Info_Invalid_or_Not_Ini_ffff0000907f57e0,s_SEC_ROMINFO_ffff0000907dbba4);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_s_ffff0000907d065b,0x57,&DAT_ffff0000907d24c9);
  }
  if (sVar1 == 2) {
    FUN_ffff00009077de54(s___s__Rom_Info_Ver____d_ffff0000907ea291,s_SEC_ROMINFO_ffff0000907dbba4,2)
    ;
    DAT_ffff000090837e94 = _DAT_ffff00009090c05c;
    DAT_ffff000090837e98 = _DAT_ffff00009090c058;
    return 0;
  }
  FUN_ffff00009077de54
            (s___s__Rom_Info_Ver_Check_Fail__Ex_ffff0000907fab4a,s_SEC_ROMINFO_ffff0000907dbba4,
             sVar1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_sboot_s_ffff0000907d065b,0x5b,&DAT_ffff0000907d24c9);
}


