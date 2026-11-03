// ffff000090783d08  size=156  FUN_ffff000090783d08

void FUN_ffff000090783d08(void)

{
  int iVar1;
  undefined8 *puVar2;
  char *pcVar3;
  
  puVar2 = (undefined8 *)FUN_ffff00009071a204(s_pl_boottag_ffff0000907f8c70,0);
  if ((puVar2 == (undefined8 *)0x0) || (iVar1 = FUN_ffff00009071a6ac(*puVar2), iVar1 == 0)) {
    puVar2 = (undefined8 *)FUN_ffff00009071a204(s_pl_drambuf_ffff0000907f35ee,0);
    if ((puVar2 == (undefined8 *)0x0) || (iVar1 = FUN_ffff00009071a6ac(*puVar2), iVar1 == 0)) {
      return;
    }
    pcVar3 = s_pl_drambuf_ffff0000907f35ee;
  }
  else {
    pcVar3 = s_pl_boottag_ffff0000907f8c70;
  }
  FUN_ffff00009077de54
            (s__s__d__free__s_failed_ffff0000907f53e9,s_mrdump_free_reserved_ffff0000907e02ca,0x3a,
             pcVar3);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_common_aee_mrd_ffff0000907e9efe,0x3b,&DAT_ffff0000907d24c9);
}


