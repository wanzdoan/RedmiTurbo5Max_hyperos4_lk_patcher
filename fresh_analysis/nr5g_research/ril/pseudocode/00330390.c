
/* RfxRadioCapabilityData::~RfxRadioCapabilityData() */

void __thiscall RfxRadioCapabilityData::~RfxRadioCapabilityData(RfxRadioCapabilityData *this)

{
  *(undefined **)this = PTR_vtable_005d20f8 + 0x10;
  if (*(void **)(this + 8) != (void *)0x0) {
    free(*(void **)(this + 8));
    return;
  }
  return;
}


