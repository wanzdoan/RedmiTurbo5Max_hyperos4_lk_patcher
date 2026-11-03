
undefined2 FUN_ffff000090721490(void)

{
  undefined2 uVar1;
  undefined1 uVar2;
  int iVar3;
  undefined8 uVar4;
  undefined1 local_24 [4];
  
  uVar4 = 0x3c;
  local_24[0] = 0x3c;
  iVar3 = FUN_ffff000090728b24(10,0x55,local_24,1,1,0);
  uVar2 = local_24[0];
  if (iVar3 == 0) {
    uVar4 = 0x3d;
    local_24[0] = 0x3d;
    iVar3 = FUN_ffff000090728b24(10,0x55,local_24,1,1,0);
    if (iVar3 == 0) {
      uVar1 = CONCAT11(local_24[0],uVar2);
      FUN_ffff00009077e11c
                (s__XMCHG_LK_BQ28Z610___s__dc__d_da_ffff0000907e28a4,
                 s_bq28z610_get_dc_ffff0000907e76ef,uVar1,local_24[0],uVar2);
      return uVar1;
    }
  }
  FUN_ffff00009077e11c
            (s__XMCHG_LK_BQ28Z610___s__i2c_read_ffff0000907e2874,
             s_bq28z610_read_byte_ffff0000907d28fb,iVar3);
  FUN_ffff00009077e11c
            (s__XMCHG_LK_BQ28Z610___s_I2C_faile_ffff0000907e90cd,s_fg_read_block_ffff00009080231d,
             uVar4);
  return 0;
}


