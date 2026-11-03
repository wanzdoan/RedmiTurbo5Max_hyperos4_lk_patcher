// ffff000090721250  size=220  FUN_ffff000090721250

undefined2 FUN_ffff000090721250(void)

{
  undefined2 uVar1;
  undefined1 uVar2;
  int iVar3;
  undefined8 uVar4;
  undefined1 local_24 [4];
  
  uVar4 = 0x3c;
  local_24[0] = 0x3c;
  iVar3 = FUN_ffff0000907288e0(10,0x55,local_24,1,1,0);
  uVar2 = local_24[0];
  if (iVar3 == 0) {
    uVar4 = 0x3d;
    local_24[0] = 0x3d;
    iVar3 = FUN_ffff0000907288e0(10,0x55,local_24,1,1,0);
    if (iVar3 == 0) {
      uVar1 = CONCAT11(local_24[0],uVar2);
      FUN_ffff00009077de54
                (s__XMCHG_LK_BQ28Z610___s__dc__d_da_ffff0000907e27bc,
                 s_bq28z610_get_dc_ffff0000907e7594,uVar1,local_24[0],uVar2);
      return uVar1;
    }
  }
  FUN_ffff00009077de54
            (s__XMCHG_LK_BQ28Z610___s__i2c_read_ffff0000907e278c,
             s_bq28z610_read_byte_ffff0000907d28be,iVar3);
  FUN_ffff00009077de54
            (s__XMCHG_LK_BQ28Z610___s_I2C_faile_ffff0000907e8f72,s_fg_read_block_ffff000090802119,
             uVar4);
  return 0;
}


