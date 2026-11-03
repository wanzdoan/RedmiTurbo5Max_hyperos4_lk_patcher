
/* ImsConfigUtils::getforceSetVoNRValue() */

int ImsConfigUtils::getforceSetVoNRValue(void)

{
  long lVar1;
  int iVar2;
  undefined8 uVar3;
  String8 aSStack_a8 [8];
  char local_a0 [104];
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  uVar3 = *(undefined8 *)PTR_PROPERTY_VONR_FORCE_ENABLE_005d2cd8;
  local_a0[0x54] = '\0';
  local_a0[0x55] = '\0';
  local_a0[0x56] = '\0';
  local_a0[0x57] = '\0';
  local_a0[0x58] = '\0';
  local_a0[0x59] = '\0';
  local_a0[0x5a] = '\0';
  local_a0[0x5b] = '\0';
  local_a0[0x50] = '\0';
  local_a0[0x51] = '\0';
  local_a0[0x52] = '\0';
  local_a0[0x53] = '\0';
  local_a0[0x18] = '\0';
  local_a0[0x19] = '\0';
  local_a0[0x1a] = '\0';
  local_a0[0x1b] = '\0';
  local_a0[0x1c] = '\0';
  local_a0[0x1d] = '\0';
  local_a0[0x1e] = '\0';
  local_a0[0x1f] = '\0';
  local_a0[0x10] = '\0';
  local_a0[0x11] = '\0';
  local_a0[0x12] = '\0';
  local_a0[0x13] = '\0';
  local_a0[0x14] = '\0';
  local_a0[0x15] = '\0';
  local_a0[0x16] = '\0';
  local_a0[0x17] = '\0';
  local_a0[0x28] = '\0';
  local_a0[0x29] = '\0';
  local_a0[0x2a] = '\0';
  local_a0[0x2b] = '\0';
  local_a0[0x2c] = '\0';
  local_a0[0x2d] = '\0';
  local_a0[0x2e] = '\0';
  local_a0[0x2f] = '\0';
  local_a0[0x20] = '\0';
  local_a0[0x21] = '\0';
  local_a0[0x22] = '\0';
  local_a0[0x23] = '\0';
  local_a0[0x24] = '\0';
  local_a0[0x25] = '\0';
  local_a0[0x26] = '\0';
  local_a0[0x27] = '\0';
  local_a0[0x38] = '\0';
  local_a0[0x39] = '\0';
  local_a0[0x3a] = '\0';
  local_a0[0x3b] = '\0';
  local_a0[0x3c] = '\0';
  local_a0[0x3d] = '\0';
  local_a0[0x3e] = '\0';
  local_a0[0x3f] = '\0';
  local_a0[0x30] = '\0';
  local_a0[0x31] = '\0';
  local_a0[0x32] = '\0';
  local_a0[0x33] = '\0';
  local_a0[0x34] = '\0';
  local_a0[0x35] = '\0';
  local_a0[0x36] = '\0';
  local_a0[0x37] = '\0';
  local_a0[0x48] = '\0';
  local_a0[0x49] = '\0';
  local_a0[0x4a] = '\0';
  local_a0[0x4b] = '\0';
  local_a0[0x4c] = '\0';
  local_a0[0x4d] = '\0';
  local_a0[0x4e] = '\0';
  local_a0[0x4f] = '\0';
  local_a0[0x40] = '\0';
  local_a0[0x41] = '\0';
  local_a0[0x42] = '\0';
  local_a0[0x43] = '\0';
  local_a0[0x44] = '\0';
  local_a0[0x45] = '\0';
  local_a0[0x46] = '\0';
  local_a0[0x47] = '\0';
  local_a0[8] = '\0';
  local_a0[9] = '\0';
  local_a0[10] = '\0';
  local_a0[0xb] = '\0';
  local_a0[0xc] = '\0';
  local_a0[0xd] = '\0';
  local_a0[0xe] = '\0';
  local_a0[0xf] = '\0';
  local_a0[0] = '\0';
  local_a0[1] = '\0';
  local_a0[2] = '\0';
  local_a0[3] = '\0';
  local_a0[4] = '\0';
  local_a0[5] = '\0';
  local_a0[6] = '\0';
  local_a0[7] = '\0';
  rfx_property_get(uVar3,local_a0,&DAT_001f9509);
  iVar2 = atoi(local_a0);
  android::String8::format
            ("getSystemPropValueWithDefault, propName:%s, value:%s",aSStack_a8,uVar3,local_a0);
  android::String8::~String8(aSStack_a8);
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return iVar2;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


